import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Hr
import org.jetbrains.compose.web.dom.Text

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("MultipleTimersComponent")
class MultipleTimersComponent : ComposedWebComponent(Factory) {

    object Factory : WebComponent.Factory<MultipleTimersComponent>(
        tagName = "multiple-timers",
        clazz = MultipleTimersComponent::class.js,
        dependencies = listOf(TimerWebComponent.Factory)
    )

    @Composable
    override fun render() {
        H1 {
            Text("Multiple timers")
            Timer(
                time = 10,
                onTimerStarted = { println("Timer started: $it") },
                onTimerEnded = { println("Timer ended: $it") },
            )
            Hr()
            Timer(20)
            Hr()
            Timer(30)
            Hr()
            Timer(40)
        }
    }
}