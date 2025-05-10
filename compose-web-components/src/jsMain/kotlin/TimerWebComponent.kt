import TimerWebComponent.Events.TimerEnded
import TimerWebComponent.Events.TimerStarted
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import org.jetbrains.compose.web.attributes.disabled
import org.jetbrains.compose.web.css.Color
import org.jetbrains.compose.web.css.DisplayStyle
import org.jetbrains.compose.web.css.Style
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.css.backgroundColor
import org.jetbrains.compose.web.css.borderRadius
import org.jetbrains.compose.web.css.color
import org.jetbrains.compose.web.css.cursor
import org.jetbrains.compose.web.css.display
import org.jetbrains.compose.web.css.em
import org.jetbrains.compose.web.css.fontFamily
import org.jetbrains.compose.web.css.fontSize
import org.jetbrains.compose.web.css.fontWeight
import org.jetbrains.compose.web.css.margin
import org.jetbrains.compose.web.css.padding
import org.jetbrains.compose.web.css.px
import org.jetbrains.compose.web.css.textDecoration
import org.jetbrains.compose.web.dom.Button
import org.jetbrains.compose.web.dom.H1
import org.jetbrains.compose.web.dom.Text
import org.jetbrains.compose.web.dom.stringPresentation
import web.dom.document

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("TimerWebComponent")
class TimerWebComponent : ComposedWebComponent(factory = Factory) {

    object Factory : WebComponent.Factory<TimerWebComponent>(
        tagName = "timer-component",
        clazz = TimerWebComponent::class.js,
        attributes = listOf(Attributes.Time),
        styleSheet = Styles,
    )

    object Attributes {
        val Time = ObservedAttribute("time", 0, { it?.toString()?.toIntOrNull() ?: 0 })
    }

    object Events {
        val TimerStarted = EventDescriptor<Int>("timerStarted")
        val TimerEnded = EventDescriptor<Int>("timerEnded")
    }

    object Styles : StyleSheet() {
        init {
            ":host" style {
                fontFamily("sans-serif")
                fontSize(12.px)
            }

            "main" style {
                padding(1.em)
                display(DisplayStyle.Block)
                property("width", "fit-content")
                backgroundColor(Color.antiquewhite)
                fontWeight("bold")
                borderRadius(0.5.em)
            }

            "button" style {
                padding(0.5.em)
                textDecoration("none")
                backgroundColor(Color.blue)
                color(Color.white)
                property("border-radius", "0.5em")
                property("border-style", "solid")
                property("border-width", "0.1em")
                property("cursor", "pointer")
                margin(0.5.em)

                disabled {
                    cursor("default")
                    backgroundColor(Color.lightgray)
                }
            }
        }
    }

    @Composable override fun render() {
        var isStarted by remember { mutableStateOf(false) }
        var currentTime by remember { mutableStateOf(0) }
        val initialTime by remember { observedAttributes[Attributes.Time] }.collectAsState()

        LaunchedEffect(isStarted) {
            if (!isStarted) return@LaunchedEffect

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

        LaunchedEffect(initialTime) { currentTime = initialTime }

        H1 { Text("Time: ${currentTime}") }
        Button(
            attrs = {
                onClick { isStarted = true }
                if (isStarted) disabled()
            }
        ) {
            Text("Start")
        }
        Button (
            attrs = {
                onClick { currentTime = initialTime }
            }
        ){
            Text("Reset")
        }
        Button(
            attrs = {
                onClick { isStarted = false }
                if (!isStarted) disabled()
            }
        ) {
            Text("Stop")
        }
    }
}