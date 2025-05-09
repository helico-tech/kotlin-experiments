import ObservedAttributes.Attribute
import js.core.JsAny
import web.components.CustomElement
import web.components.CustomElementConstructor
import web.components.ShadowRoot
import web.components.ShadowRootInit
import web.components.ShadowRootMode
import web.components.customElements
import web.html.HTMLElement
import web.html.HtmlTagName


abstract class WebComponent(val shadowRootMode: ShadowRootMode, vararg attributes: Attribute<*>) : HTMLElement(), CustomElement.WithCallbacks {
    val observedAttributes = ObservedAttributes(*attributes)
    val shadow: ShadowRoot = this.attachShadow(ShadowRootInit(mode = shadowRootMode))

    override fun attributeChangedCallback(name: String, oldValue: JsAny?, newValue: JsAny?) {
        observedAttributes.update(name, oldValue, newValue)
    }
}

interface WebComponentFactory<T : WebComponent> {
    val tagName: HtmlTagName<T>
    val clazz: CustomElementConstructor<T>
    val observedAttributes: Array<Attribute<*>>

    fun register() {
        clazz.asDynamic().observedAttributes = observedAttributes.map { it.name }.toTypedArray()
        customElements.define(tagName, clazz)
    }
}

fun registerWebComponents(vararg factories: WebComponentFactory<*>) = factories.forEach { it.register() }