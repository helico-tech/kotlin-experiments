import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import web.events.CustomEvent
import web.events.CustomEventInit
import web.events.EventType
import web.html.HtmlTagName

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("CounterComponent")
class CounterComponent : ComposeWebComponent(COUNTER, MIN, MAX) {

    @JsExport.Ignore
    companion object : WebComponentFactory<CounterComponent> {
        val COUNTER = ObservedAttributes.Attribute("counter", 0, cast = { it?.toInt() ?: 0 })
        val MIN = ObservedAttributes.Attribute("min", 0, cast = { it?.toInt() ?: 0 })
        val MAX = ObservedAttributes.Attribute("max", 100, cast = { it?.toInt()?: 100 })

        override val tagName = HtmlTagName<CounterComponent>("counter-component")
        override val clazz = CounterComponent::class.js
        override val observedAttributes = ObservedAttributes.of(COUNTER, MIN, MAX)
    }

    private fun maxExceeded(max: Int) {
        val event = CustomEvent(EventType("max-exceeded"), CustomEventInit(detail = max))
        this.dispatchEvent(event)
    }

    private fun minExceeded(min: Int) {
        val event = CustomEvent(EventType("min-exceeded"), CustomEventInit(detail = min))
        this.dispatchEvent(event)
    }

    @Composable
    override fun content() {
        val min by observedAttributes[MIN]!!.collectAsState()
        val max by observedAttributes[MAX]!!.collectAsState()
        val counter by observedAttributes[COUNTER]!!.collectAsState()

        if (counter > max) {
            maxExceeded(counter)
        }

        if (counter < min) {
            minExceeded(counter)
        }

        H1 { Text("Counter: ${counter.coerceIn(min, max)}") }
    }

    override fun connectedCallback() {

    }

    override fun disconnectedCallback() {}

    override fun adoptedCallback() {}
}