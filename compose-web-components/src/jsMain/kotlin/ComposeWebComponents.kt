import ObservedAttributes.Attribute
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import org.jetbrains.compose.web.renderComposable
import org.w3c.dom.HTMLElement
import web.components.ShadowRootMode
import web.dom.document

val LocalWebComponent = compositionLocalOf<web.html.HTMLElement> { error("No root element found!") }

abstract class ComposeWebComponent(vararg attributes: Attribute<*>) : WebComponent(ShadowRootMode.closed, *attributes) {
    @Composable() abstract fun content()

    init {
        val main = document.createElement("main")
        shadow.appendChild(main)

        renderComposable(root = main as HTMLElement) {
            CompositionLocalProvider(
                LocalObservedAttributes provides observedAttributes,
                LocalWebComponent provides this@ComposeWebComponent,
            ) {
                content()
            }
        }
    }
}