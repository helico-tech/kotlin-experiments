import com.typesafe.config.ConfigFactory
import kafka.KafkaService
import kotlinx.coroutines.runBlocking

fun main() {
    val config = ConfigFactory.load()
    val kafkaService = KafkaService(config)

    runBlocking {
        val consumerOffsetsJson = kafkaService.consumerOffsets()
        println("Consumer offsets for all topics:")
        println(consumerOffsetsJson)
    }
}
