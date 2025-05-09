import js.core.JsAny
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import web.components.CustomElement

data class ObservedAttribute<T>(
    val name: String,
    val default: T,
    val cast: (JsAny?) -> T
)

class ObservedAttributes(
    attributes: List<ObservedAttribute<*>>
) : CustomElement.WithAttributeChangedCallback {

    constructor(vararg attributes: ObservedAttribute<*>) : this(attributes.toList())

    private val names = attributes.associateBy { it.name }
    private val flows = attributes.associate { it to MutableStateFlow(it.default) }

    override fun attributeChangedCallback(name: String, oldValue: JsAny?, newValue: JsAny?) {
        val name = requireNotNull(names[name]) { "Unknown attribute: $name" }
        val flow = requireNotNull(flows[name]) { "Unknown attribute: $name" }
        flow.update { name.cast(newValue) }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getOrNull(attr: ObservedAttribute<T>): StateFlow<T>? = flows[attr] as? StateFlow<T>

    operator fun <T> get(attr: ObservedAttribute<T>): StateFlow<T> = requireNotNull(getOrNull(attr)) { "Unknown attribute: $attr" }
}