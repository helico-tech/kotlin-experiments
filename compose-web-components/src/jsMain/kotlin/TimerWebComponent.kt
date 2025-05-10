import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.renderComposable
import web.events.CustomEvent
import web.events.CustomEventInit
import web.events.EventType

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("TimerWebComponent")
class TimerWebComponent : WebComponent(
    factory = Factory
) {
    object Attributes {
        val Time = ObservedAttribute("time", 0, { it?.toString()?.toIntOrNull() ?: 0 })
    }

    object Factory : WebComponent.Factory<TimerWebComponent>(
        tagName = "timer-component",
        clazz = TimerWebComponent::class.js,
        attributes = listOf(Attributes.Time),
    )

    override fun connectedCallback() {
        renderComposable(root as org.w3c.dom.HTMLElement) {
            var currentTime by remember { mutableStateOf(0) }
            val initialTime by observedAttributes[Attributes.Time].collectAsState()

            LaunchedEffect(initialTime) {
                currentTime = initialTime

                while (true) {
                    if (currentTime == initialTime) dispatchTimerStarted(initialTime)

                    delay(1000)

                    currentTime -= 1
                    currentTime = currentTime.coerceAtLeast(0)

                    if (currentTime == 0) {
                        dispatchTimerEnded(initialTime)
                        break
                    }
                }
            }

            H1 { Text("Time: ${currentTime}") }
        }
    }

    private fun dispatchTimerStarted(initialTime: Int) {
        val event = CustomEvent(EventType("timerStarted"), CustomEventInit(detail = initialTime, bubbles = true, composed = true))
        this.dispatchEvent(event)
    }

    private fun dispatchTimerEnded(initialTime: Int) {
        val event = CustomEvent(EventType("timerEnded"), CustomEventInit(detail = initialTime, bubbles = true, composed = true))
        this.dispatchEvent(event)
    }
}