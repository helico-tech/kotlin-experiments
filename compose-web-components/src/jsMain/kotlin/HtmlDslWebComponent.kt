import kotlinx.html.TagConsumer
import kotlinx.html.dom.append
import org.w3c.dom.HTMLElement
import web.components.ShadowRootMode

abstract class HtmlDslWebComponent(
    factory: Factory<out WebComponent>,
    mode: ShadowRootMode = ShadowRootMode.closed,
    observedAttributes: ObservedAttributes = ObservedAttributes(factory.attributes),
    rootElementTagName: String = "main",
) : WebComponent(factory, mode, observedAttributes, rootElementTagName) {
    override fun connectedCallback() {
        redraw()
    }

    fun redraw() {
        root.innerHTML = ""
        (root as HTMLElement).append {
            render()
        }
    }

    abstract fun TagConsumer<HTMLElement>.render()
}