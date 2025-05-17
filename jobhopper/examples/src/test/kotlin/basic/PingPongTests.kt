package basic

import kotlinx.coroutines.test.runTest
import nl.helico.jobhopper.core.LiteralDataSource
import nl.helico.jobhopper.core.component1
import nl.helico.jobhopper.core.component2
import kotlin.test.Test
import kotlin.test.assertEquals

class PingPongTests {

    @Test
    fun ping() = runTest {
        val (source, sink) = LiteralDataSource(PING)

        val job = PingPong(
            source = source,
            sink = sink,
        )

        job.execute()
        assertEquals(source.get(), PONG)
    }
}