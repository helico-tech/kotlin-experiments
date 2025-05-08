import androidx.compose.runtime.Composable
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.remember
import web.events.CustomEvent
import web.events.CustomEventInit
import web.events.EventType
import web.html.HTMLElement

class EventDispatcher<T>(val origin: HTMLElement, val descriptor: EventDescriptor<T>) {
    operator fun invoke(payload: T) {
        val event = CustomEvent<T>(
            type = EventType(descriptor.name),
            init = CustomEventInit(detail = payload, bubbles = descriptor.bubbles, cancelable = descriptor.cancellable, composed = descriptor.composed)
        )
        origin.dispatchEvent(event)
    }
}

data class EventDescriptor<T>(val name: String, val bubbles: Boolean? = null, val cancellable: Boolean? = null, val composed: Boolean? = null)

fun <T> Event(name: String, bubbles: Boolean? = null, cancellable: Boolean? = null, composed: Boolean? = null) = EventDescriptor<T>(name, bubbles, cancellable, composed)

@Composable fun <T> eventDispatcher(descriptor: EventDescriptor<T>): EventDispatcher<T> {
    val element = LocalWebComponent.current
    return remember {
        EventDispatcher(element, descriptor)
    }
}