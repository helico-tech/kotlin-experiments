import ObservedAttributes.Attribute
import androidx.compose.runtime.Composable
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLElement
import web.components.ShadowRootInit
import web.components.ShadowRootMode
import web.dom.document

abstract class ComposeWebComponent(vararg attributes: Attribute<*>) : WebComponent(ShadowRootMode.closed, *attributes) {
    @Composable() abstract fun content()

    init {
        val main = document.createElement("main")

        shadow.appendChild(main)

        renderComposable(root = main as HTMLElement) {
            content()
        }
    }
}