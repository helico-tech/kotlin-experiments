import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import web.html.HtmlTagName

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("CounterComponent")
class CounterComponent : ComposeWebComponent(*observedAttributes) {

    @JsExport.Ignore
    companion object : WebComponentFactory<CounterComponent> {
        val COUNTER = ObservedAttribute("counter", 0, cast = { it?.toInt() ?: 0 })
        val MIN = ObservedAttribute("min", 0, cast = { it?.toInt() ?: 0 })
        val MAX = ObservedAttribute("max", 100, cast = { it?.toInt()?: 100 })

        val MIN_EXCEEDED = Event<Int>("min-exceeded", bubbles = true, composed = true)
        val MAX_EXCEEDED = Event<Int>("max-exceeded", bubbles = true)

        override val tagName = HtmlTagName<CounterComponent>("counter-component")
        override val clazz = CounterComponent::class.js
        override val observedAttributes : Array<ObservedAttributes.Attribute<*>> = arrayOf(COUNTER, MIN, MAX)
    }

    @Composable
    override fun content() {
        val min by observedAttribute(MIN)
        val max by observedAttribute(MAX)
        val counter by observedAttribute(COUNTER)

        val onMinExceeded = eventDispatcher(MIN_EXCEEDED)
        val onMaxExceeded = eventDispatcher(MAX_EXCEEDED)

        LaunchedEffect(counter) {
            if (counter < min) {
                onMinExceeded(min)
            } else if (counter > max) {
                onMaxExceeded(max)
            }
        }

        H1 { Text("Counter: ${counter.coerceIn(min, max)}") }
    }

    override fun connectedCallback() {}
    override fun disconnectedCallback() {}
    override fun adoptedCallback() {}
}