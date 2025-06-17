import ai.koog.agents.core.agent.AIAgent
import ai.koog.agents.core.dsl.builder.AIAgentEdgeBuilderIntermediate
import ai.koog.agents.core.dsl.builder.forwardTo
import ai.koog.agents.core.dsl.builder.strategy
import ai.koog.agents.core.dsl.extension.nodeDoNothing
import ai.koog.agents.core.dsl.extension.nodeExecuteMultipleTools
import ai.koog.agents.core.dsl.extension.nodeLLMRequestMultiple
import ai.koog.agents.core.dsl.extension.nodeLLMSendMultipleToolResults
import ai.koog.agents.core.dsl.extension.onAssistantMessage
import ai.koog.agents.core.dsl.extension.onIsInstance
import ai.koog.agents.core.tools.ToolRegistry
import ai.koog.agents.core.tools.reflect.asTools
import ai.koog.agents.ext.tool.SayToUser
import ai.koog.prompt.executor.llms.all.simpleOllamaAIExecutor
import ai.koog.prompt.llm.OllamaModels
import ai.koog.prompt.message.Message
import com.typesafe.config.ConfigFactory
import kafka.KafkaService
import kafka.KafkaToolSet
import kotlinx.coroutines.runBlocking

private fun alternativeSingleRunStrategy() =
    strategy("alternativeSingleRunStrategy") {
        val initialRequest by nodeLLMRequestMultiple()
        val processResponses by nodeDoNothing<List<Message.Response>>()
        val executeTools by nodeExecuteMultipleTools(parallelTools = true)
        val toolResultsRequest by nodeLLMSendMultipleToolResults()

        edge(nodeStart forwardTo initialRequest)
        edge(initialRequest forwardTo processResponses)

        edge(processResponses forwardTo executeTools onToolCallsPresent { true })
        edge(processResponses forwardTo nodeFinish transformed { it.first() } onAssistantMessage { true })

        edge(executeTools forwardTo toolResultsRequest)
        edge(toolResultsRequest forwardTo processResponses)
    }

infix fun <IncomingOutput, IntermediateOutput, OutgoingInput> AIAgentEdgeBuilderIntermediate<IncomingOutput, IntermediateOutput, OutgoingInput>.onToolCallsPresent(
    block: suspend (List<Message.Tool.Call>) -> Boolean,
): AIAgentEdgeBuilderIntermediate<IncomingOutput, List<Message.Tool.Call>, OutgoingInput> =
    onIsInstance(List::class)
        .transformed { it.filterIsInstance<Message.Tool.Call>() }
        .onCondition { toolCalls -> toolCalls.isNotEmpty() && block(toolCalls) }

fun main() {
    val config = ConfigFactory.load()
    val kafkaService = KafkaService(config)

    val toolRegistry =
        ToolRegistry {
            tools(KafkaToolSet(kafkaService).asTools() + listOf(SayToUser))
        }

    val agent =
        AIAgent(
            executor = simpleOllamaAIExecutor(),
            systemPrompt = "You a diagnostics agent. You can answer questions about the workings of systems provided to you via tools.",
            llmModel = OllamaModels.Alibaba.QWEN_3_06B,
            toolRegistry = toolRegistry,
            maxIterations = 20,
            strategy = alternativeSingleRunStrategy(),
        )

    runBlocking {
        val result = agent.runAndGetResult("Get me the highest offset of any consumer")
        println(result)
    }
}
