import js.core.JsAny
import web.components.CustomElement
import web.components.ShadowRoot
import web.components.ShadowRootInit
import web.components.ShadowRootMode
import web.html.HTMLElement

abstract class WebComponent(shadowRootMode: ShadowRootMode) : HTMLElement(), CustomElement.WithCallbacks {
    val shadow: ShadowRoot = this.attachShadow(ShadowRootInit(mode = shadowRootMode))

    override fun connectedCallback() {
        println(this.shadow)
    }
    override fun disconnectedCallback() {}
    override fun adoptedCallback() {}
    override fun attributeChangedCallback(name: String, oldValue: JsAny?, newValue: JsAny?) {}
}