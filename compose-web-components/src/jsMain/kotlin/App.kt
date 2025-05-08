import js.core.JsAny
import js.temporal.Duration
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.Element
import web.components.CustomElement
import web.components.ShadowRootInit
import web.components.ShadowRootMode
import web.components.customElements
import web.dom.document
import web.html.HTMLElement
import web.html.HtmlTagName
import web.timers.setInterval
import kotlin.random.Random
import kotlin.time.Duration.Companion.milliseconds

class MyWebComponent : HTMLElement(), CustomElement.WithConnectedCallback, CustomElement.WithAttributeChangedCallback {

    companion object {
        val observedAttributes = arrayOf("counter")

        fun register() {
            MyWebComponent::class.js.asDynamic().observedAttributes = observedAttributes
            customElements.define(HtmlTagName("my-web-component"), MyWebComponent::class.js)
        }
    }

    override fun connectedCallback() {
        val shadow = this.attachShadow(ShadowRootInit(mode = ShadowRootMode.open))
        val root = document.createElement("div")
        root.id = "compose-root"
        shadow.appendChild(root)

        val actualRoot = shadow.getElementById("compose-root") as Element

        renderComposable(root = actualRoot) {
            Text("Hello World!")
        }
    }

    override fun attributeChangedCallback(name: String, oldValue: JsAny?, newValue: JsAny?) {
        println("Attribute changed: $name, $oldValue, $newValue")
    }
}

fun main() {
    MyWebComponent.register()

    val myElement = document.createElement("my-web-component")
    myElement.setAttribute("counter", "1")

    document.body.appendChild(myElement)

    setInterval(timeout = 1000.milliseconds) {
        myElement.setAttribute("counter", Random.nextInt(1, 100).toString())
        println("Interval!")
    }
}