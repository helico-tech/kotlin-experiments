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

class MyWebComponent : HTMLElement(), CustomElement.WithConnectedCallback {
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
}

fun main() {
    customElements.define(HtmlTagName("my-web-component"), MyWebComponent::class.js)
}