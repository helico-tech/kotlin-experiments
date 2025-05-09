import web.components.customElements
import web.html.HtmlTagName

fun main() {
    customElements.define(HtmlTagName("timer-component"), TimerWebComponent::class.js)
}