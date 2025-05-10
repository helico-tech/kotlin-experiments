import TimerWebComponent.Events.TimerEnded
import TimerWebComponent.Events.TimerStarted
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

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("TimerWebComponent")
class TimerWebComponent : WebComponent(factory = Factory) {

    object Factory : WebComponent.Factory<TimerWebComponent>(
        tagName = "timer-component",
        clazz = TimerWebComponent::class.js,
        attributes = listOf(Attributes.Time),
    )

    object Attributes {
        val Time = ObservedAttribute("time", 0, { it?.toString()?.toIntOrNull() ?: 0 })
    }

    object Events {
        val TimerStarted = EventDescriptor<Int>("timerStarted")
        val TimerEnded = EventDescriptor<Int>("timerEnded")
    }

    override fun connectedCallback() {
        renderComposable(root as org.w3c.dom.HTMLElement) {
            var currentTime by remember { mutableStateOf(0) }
            val initialTime by observedAttributes[Attributes.Time].collectAsState()

            LaunchedEffect(initialTime) {
                currentTime = initialTime

                while (true) {
                    if (currentTime == initialTime) dispatchEvent(TimerStarted, initialTime)

                    delay(1000)

                    currentTime -= 1
                    currentTime = currentTime.coerceAtLeast(0)

                    if (currentTime == 0) {
                        dispatchEvent(TimerEnded, initialTime)
                        break
                    }
                }
            }

            H1 { Text("Time: ${currentTime}") }
        }
    }
}