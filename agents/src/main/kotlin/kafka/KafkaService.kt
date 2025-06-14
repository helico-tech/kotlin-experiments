package kafka

import com.typesafe.config.Config
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.apache.kafka.clients.admin.AdminClient
import org.apache.kafka.clients.admin.KafkaAdminClient
import org.apache.kafka.clients.admin.OffsetSpec
import org.apache.kafka.clients.consumer.OffsetAndMetadata
import org.apache.kafka.common.TopicPartition
import java.time.Duration
import java.util.concurrent.ExecutionException

@Serializable
data class PartitionOffsetInfo(
    val offset: Long,
    val lag: Long
)

@Serializable
data class ConsumerGroupOffset(
    val groupId: String,
    val topicPartitions: Map<String, Map<Int, PartitionOffsetInfo>>
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

            // Get all topic partitions for which we have consumer offsets
            val topicPartitions = offsetsMap.keys

            // Get the end offsets (latest offsets) for these partitions
            val endOffsetsMap = adminClient.listOffsets(
                topicPartitions.associateWith { OffsetSpec.latest() }
            ).all().get()

            // Group by topic and create PartitionOffsetInfo objects with lag calculation
            val topicPartitionsMap = offsetsMap.entries.groupBy(
                { it.key.topic() },
                { entry -> 
                    val partition = entry.key.partition()
                    val consumerOffset = entry.value.offset()
                    val endOffset = endOffsetsMap[entry.key]?.offset() ?: consumerOffset
                    val lag = maxOf(0L, endOffset - consumerOffset)

                    partition to PartitionOffsetInfo(consumerOffset, lag)
                }
            ).mapValues { (_, partitionOffsets) ->
                partitionOffsets.toMap()
            }

            ConsumerGroupOffset(groupId, topicPartitionsMap)
        }

        return Json.encodeToString(allOffsets)
    }
}
