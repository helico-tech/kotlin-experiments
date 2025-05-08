import js.core.JsAny
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ObservedAttributes(vararg attributes: Attribute<*>) {

    data class Attribute<T>(
        val name: String,
        val default: T,
        val cast: (String?) -> T
    )

    private val attributeNames = mutableMapOf<String, Attribute<*>>()
    private val attributes = mutableMapOf<Attribute<*>, MutableStateFlow<*>>()

    init {
        attributes.forEach {
            this.attributes[it] = MutableStateFlow(it.default)
            attributeNames[it.name] = it
        }
    }

    companion object {
        fun <T> of(vararg attributes: Attribute<T>) = attributes.map { it.name }.toTypedArray()
    }

    operator fun <T> get(attribute: Attribute<T>) : StateFlow<T>? {
        @Suppress("UNCHECKED_CAST")
        return attributes[attribute] as? MutableStateFlow<T>
    }

    val names get() = attributes.keys.map { it.name }.toTypedArray()

    fun update(name: String, _oldValue: JsAny?, newValue: JsAny?) {
        val key = attributeNames[name] ?: return
        @Suppress("UNCHECKED_CAST")
        val attribute = attributes[key] as? MutableStateFlow<Any?> ?: return
        attribute.value = key.cast(newValue?.toString())
    }
}