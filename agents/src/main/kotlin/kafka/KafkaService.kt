package kafka

import com.typesafe.config.Config
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.KafkaAdminClient
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import java.util.concurrent.ExecutionException

@Serializable
data class ConsumerGroupOffset(
    val groupId: String,
    val topicPartitions: Map<String, Map<Int, Long>>
)

class KafkaService(
    val adminClient: AdminClient,
) {
    companion object {
        operator fun invoke(config: Config): KafkaService {
            val bootstrapServers = config.getString("kafka.bootstrapServers")
            val adminClient = KafkaAdminClient.create(mapOf("bootstrap.servers" to bootstrapServers))
            return KafkaService(adminClient)
        }
    }

    suspend fun consumerOffsets(): String {
        val consumerGroups = try {
            adminClient.listConsumerGroups().all().get().map { it.groupId() }
        } catch (e: ExecutionException) {
            emptyList<String>()
        }

        val allOffsets = consumerGroups.map { groupId ->
            val offsetsMap = adminClient.listConsumerGroupOffsets(groupId).partitionsToOffsetAndMetadata().get()

            val topicPartitionsMap = offsetsMap.entries.groupBy(
                { it.key.topic() },
                { it.key.partition() to it.value.offset() }
            ).mapValues { (_, partitionOffsets) ->
                partitionOffsets.toMap()
            }

            ConsumerGroupOffset(groupId, topicPartitionsMap)
        }

        return Json.encodeToString(allOffsets)
    }
}
