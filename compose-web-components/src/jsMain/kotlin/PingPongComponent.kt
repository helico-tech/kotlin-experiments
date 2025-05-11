import kotlinx.html.TagConsumer
import kotlinx.html.button
import kotlinx.html.h1
import kotlinx.html.js.onClickFunction
import org.w3c.dom.HTMLElement

@OptIn(ExperimentalJsExport::class)
@JsExport
@JsName("PingPongComponent")
class PingPongComponent : HtmlDslWebComponent(factory = Factory) {

    object Factory : WebComponent.Factory<PingPongComponent>(
        tagName = "ping-pong",
        clazz = PingPongComponent::class.js,
    )

    private var pingOrPong = "ping"

    override fun TagConsumer<HTMLElement>.render() {
        h1 {
            +pingOrPong
        }

        button {
            + "Clicky"

            onClickFunction = {
                pingOrPong = if (pingOrPong == "ping") "pong" else "ping"
                redraw()
            }
        }
    }
}