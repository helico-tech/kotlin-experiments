package basic

import nl.helico.jobhopper.core.DataSink
import nl.helico.jobhopper.core.DataSource
import nl.helico.jobhopper.core.Job

internal const val PONG = "pong"
internal const val PING = "ping"

data class PingPong(
    override val source: DataSource<String>,
    override val sink: DataSink<String>
) : Job<String>() {
    override suspend fun execute() {
        if (source.get() == PING) sink.put(PONG) else sink.put(PING)
    }
}