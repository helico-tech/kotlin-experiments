import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import js.core.JsAny
import kotlinx.coroutines.awaitAnimationFrame
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import web.components.CustomElement
import web.components.ShadowRoot
import web.components.ShadowRootInit
import web.components.ShadowRootMode
import web.cssom.attr
import web.dom.document
import web.html.HTMLElement

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("TimerWebComponent")
class TimerWebComponent : HTMLElement(), CustomElement.WithCallbacks {

    companion object {
        val attributes = arrayOf(Attributes.Time)

        @OptIn(ExperimentalJsStatic::class)
        @JsStatic()
        @JsName("observedAttributes")
        val staticObservedAttributes = attributes.map { it.name }.toTypedArray()

        object Attributes {
            val Time = ObservedAttribute("time", 0, { it?.toString()?.toIntOrNull() ?: 0 })
        }
    }

    val shadow: ShadowRoot = this.attachShadow(ShadowRootInit(mode = ShadowRootMode.closed))
    val observedAttributes = ObservedAttributes(*TimerWebComponent.attributes)

    override fun connectedCallback() {
        val root = document.createElement("main").apply {
            shadow.appendChild(this)
        }

        renderComposable(root as org.w3c.dom.HTMLElement) {
            var currentTime by remember { mutableStateOf(0) }
            val initialTime by observedAttributes[Attributes.Time].collectAsState()

            LaunchedEffect(initialTime) {
                currentTime = initialTime
                while (true) {
                    currentTime -= 1
                    currentTime = currentTime.coerceAtLeast(0)
                    delay(1000)
                }
            }

            H1 { Text("Time: ${currentTime}") }
        }
    }

    override fun attributeChangedCallback(name: String, oldValue: JsAny?, newValue: JsAny?) {
        observedAttributes.attributeChangedCallback(name, oldValue, newValue)
    }

    override fun disconnectedCallback() {
        println("TimerWebComponent disconnected!")
    }

    override fun adoptedCallback() {
        println("TimerWebComponent adopted!")
    }
}