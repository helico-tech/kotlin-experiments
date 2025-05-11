package basic

import kotlinx.coroutines.test.runTest
import nl.helico.jobhopper.core.InMemoryDataSourceSink
import kotlin.test.Test
import kotlin.test.assertEquals

class PingPongTests {

    @Test
    fun ping() = runTest {
        val sourceSink = InMemoryDataSourceSink(PING)

        val job = PingPong(
            source = sourceSink,
            sink = sourceSink,
        )

        job.execute()
        assertEquals(sourceSink.get(), PONG)
    }
}