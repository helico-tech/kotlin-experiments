import js.core.JsAny
import web.components.CustomElement
import web.components.CustomElementConstructor
import web.components.ShadowRoot
import web.components.ShadowRootInit
import web.components.ShadowRootMode
import web.components.customElements
import web.dom.document
import web.html.HTMLElement
import web.html.HtmlTagName

abstract class WebComponent(
    factory: Factory<out WebComponent>,
    mode: ShadowRootMode = ShadowRootMode.closed,
    val observedAttributes: ObservedAttributes = ObservedAttributes(factory.attributes)
) : HTMLElement(), CustomElement.WithCallbacks, CustomElement.WithAttributeChangedCallback by observedAttributes {

    abstract class Factory<T : WebComponent>(
        val tagName: String,
        val clazz: CustomElementConstructor<T>,
        val attributes: List<ObservedAttribute<*>> = emptyList(),
    ) {
        fun register() {
            clazz.asDynamic().observedAttributes = attributes.map { it.name }.toTypedArray()
            customElements.define(HtmlTagName(tagName), clazz)
        }
    }

    val shadow: ShadowRoot = this.attachShadow(ShadowRootInit(mode = mode))

    val root = document.createElement("main").apply {
        shadow.appendChild(this)
    }

    override fun disconnectedCallback() {}
    override fun adoptedCallback() {}
}