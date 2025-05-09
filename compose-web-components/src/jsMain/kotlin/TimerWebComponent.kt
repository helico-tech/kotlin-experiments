import js.core.JsAny
import web.components.CustomElement
import web.components.ShadowRoot
import web.components.ShadowRootInit
import web.components.ShadowRootMode
import web.dom.document
import web.html.HTMLElement

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("TimerWebComponent")
class TimerWebComponent : HTMLElement(), CustomElement.WithCallbacks {

    val shadow: ShadowRoot = this.attachShadow(ShadowRootInit(mode = ShadowRootMode.closed))

    override fun connectedCallback() {
        document.createElement("h1").apply {
            innerText = "Hello from WebComponent"
            shadow.appendChild(this)
        }
    }
    override fun disconnectedCallback() {
        println("TimerWebComponent disconnected!")
    }
    override fun adoptedCallback() {
        println("TimerWebComponent adopted!")
    }
    override fun attributeChangedCallback(name: String, oldValue: JsAny?, newValue: JsAny?) {
        println("TimerWebComponent attributeChangedCallback $name $oldValue $newValue")
    }
}