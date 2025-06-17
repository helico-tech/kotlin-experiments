package kafka

import ai.koog.agents.core.tools.annotations.LLMDescription
import ai.koog.agents.core.tools.annotations.Tool
import ai.koog.agents.core.tools.reflect.ToolSet
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@LLMDescription("Tools for getting information about the Kafka cluster and its consumers")
class KafkaToolSet(
    val kafkaService: KafkaService,
) : ToolSet {
    @Tool()
    @LLMDescription("Get the consumer offsets and lag for all consumers. It will return a stringified JSON object")
    suspend fun getConsumerOffsetsAndLags(): String {
        val offsets = kafkaService.consumerOffsets()
        return Json.encodeToString(offsets)
    }
}
