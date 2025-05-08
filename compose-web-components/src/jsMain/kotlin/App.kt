import androidx.compose.runtime.collectAsState
import js.core.JsAny
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
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

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("MyWebComponent")
class MyWebComponent : HTMLElement(), CustomElement.WithConnectedCallback, CustomElement.WithAttributeChangedCallback {

    companion object {
        val observedAttributes = arrayOf("counter")

        fun register() {
            MyWebComponent::class.js.asDynamic().observedAttributes = observedAttributes
            customElements.define(HtmlTagName("my-web-component"), MyWebComponent::class.js)
        }
    }

    @JsExport.Ignore
    lateinit var counterFlow : MutableStateFlow<Int>;

    override fun connectedCallback() {
        val shadow = this.attachShadow(ShadowRootInit(mode = ShadowRootMode.open))
        val root = document.createElement("div")
        root.id = "compose-root"
        shadow.appendChild(root)

        this.counterFlow = MutableStateFlow(this.getAttribute("counter")?.toIntOrNull() ?: 0)

        renderComposable(root = root as Element) {
            val counter = counterFlow.collectAsState()
            Text("Counter: ${counter.value}")
        }
    }

    override fun attributeChangedCallback(name: String, oldValue: JsAny?, newValue: JsAny?) {
        console.log("Attribute changed: $name, $oldValue, $newValue")
        if (name == "counter") {
            counterFlow?.update { newValue?.toString()?.toIntOrNull() ?: 0 }
        }
    }
}

fun main() {
    MyWebComponent.register()
}