import js.core.JsAny
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.dom.stringPresentation
import web.components.CustomElement
import web.components.CustomElementConstructor
import web.components.ShadowRoot
import web.components.ShadowRootInit
import web.components.ShadowRootMode
import web.components.customElements
import web.dom.document
import web.events.CustomEvent
import web.events.CustomEventInit
import web.events.EventType
import web.html.HTMLElement
import web.html.HtmlTagName

abstract class WebComponent(
    factory: Factory<out WebComponent>,
    mode: ShadowRootMode = ShadowRootMode.closed,
    protected val observedAttributes: ObservedAttributes = ObservedAttributes(factory.attributes),
    rootElementTagName: String = "main",
) : HTMLElement(), CustomElement.WithCallbacks, CustomElement.WithAttributeChangedCallback by observedAttributes {

    abstract class Factory<T : WebComponent>(
        val tagName: String,
        val clazz: CustomElementConstructor<T>,
        val attributes: List<ObservedAttribute<*>> = emptyList(),
        val styleSheet: StyleSheet? = null,
    ) {
        fun register() {
            clazz.asDynamic().observedAttributes = attributes.map { it.name }.toTypedArray()
            customElements.define(HtmlTagName(tagName), clazz)
        }
    }

    data class ObservedAttribute<T>(
        val name: String,
        val default: T,
        val cast: (JsAny?) -> T
    )

    class ObservedAttributes(
        attributes: List<ObservedAttribute<*>>
    ) : CustomElement.WithAttributeChangedCallback {

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

    data class EventDescriptor<T>(val name: String, val bubbles: Boolean = true, val cancellable: Boolean? = null, val composed: Boolean = true)

    val shadow: ShadowRoot = this.attachShadow(ShadowRootInit(mode = mode))

    init {
        factory.styleSheet?.let {
            document.createElement("style").apply {
                textContent = factory.styleSheet.cssRules.joinToString(separator = "\n\n") { it.stringPresentation() }
                shadow.appendChild(this)
            }
        }
    }

    val root = document.createElement(rootElementTagName).apply {
        shadow.appendChild(this)
    }

    fun <T> dispatchEvent(descriptor: EventDescriptor<T>, payload: T) = this.dispatchEvent(
        CustomEvent(
            type = EventType(descriptor.name),
            init = CustomEventInit(
                bubbles = descriptor.bubbles,
                cancelable = descriptor.cancellable,
                composed = descriptor.composed,
                detail = payload,
            )
        )
    )

    override fun disconnectedCallback() {}
    override fun adoptedCallback() {}
}