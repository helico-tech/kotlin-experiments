import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.css.StyleSheet
import org.jetbrains.compose.web.renderComposable
import web.components.ShadowRootMode

abstract class ComposedWebComponent(
    factory: Factory<out WebComponent>,
    mode: ShadowRootMode = ShadowRootMode.closed,
    observedAttributes: ObservedAttributes = ObservedAttributes(factory.attributes),
    rootElementTagName: String = "main",
) : WebComponent(factory, mode, observedAttributes, rootElementTagName) {
    override fun connectedCallback() {
        renderComposable(root as org.w3c.dom.HTMLElement) {
            render()
        }
    }

    @Composable abstract fun render()
}