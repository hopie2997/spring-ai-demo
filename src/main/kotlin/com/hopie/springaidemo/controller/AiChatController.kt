package com.hopie.springaidemo.controller

import org.springframework.ai.chat.client.ChatClient
import org.springframework.ai.chat.prompt.PromptTemplate
import org.springframework.ai.converter.BeanOutputConverter
import org.springframework.ai.openai.OpenAiChatOptions
import org.springframework.ai.vectorstore.PgVectorStore
import org.springframework.ai.vectorstore.SimpleVectorStore
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.nio.charset.Charset

@RestController
@RequestMapping("/api/chat")
class AiChatController(
    private val chatClient: ChatClient
) {
    @Value("classpath:/prompts/youtube.st")
    private lateinit var youtubePromptResource: Resource

    @Value("classpath:/prompts/mfv.st")
    private lateinit var mfvPromptResource: Resource

    @Value("classpath:/data/mfv-annual-health-check-2024.txt")
    private lateinit var mfvHealthCheckData: Resource

    @Autowired
    private lateinit var simpleVectorStore: SimpleVectorStore

    @Autowired
    private lateinit var pgVectorStore: PgVectorStore

    @GetMapping("/mfv")
    fun mfvQnA(
        @RequestParam(defaultValue = "Give me the list of the employees in MFV") query: String
    ): Any {
        var response: Any? = "No response"

        // uncomment the following line to test
        /*

        // Ask anything
        response = simplePrompt(query)

        // Ask about top 10 videos on youtube about a topic, the response is in a structured format
        response = structuredOutput(query)

        // stuffing the prompt with the provided data
        response = simplePromptStuffing(query)
        response = promptStuffingWithSimpleVectorStore(query)
        response = promptStuffingWithPgVectorStore(query)

        // ask the MFV employees list
        response = functionCalling(query)

        * */

        return response ?: "No response"
    }

    /**
     * Use youtubePromptTemplate to get the top 10 most watched videos on youtube
     * @return a structured output
     * */
    fun structuredOutput(topic: String): YouTubeChatResponse? {
        val parser = BeanOutputConverter(YouTubeChatResponse::class.java)
        val prompt = PromptTemplate(
            youtubePromptResource,
            mapOf("topic" to topic, "format" to parser.format)
        ).create()

        val chatResponse = chatClient.prompt(prompt).call().content() ?: ""
        return parser.convert(chatResponse)
    }

    class YouTubeChatResponse(
        val title: String,
        val link: String,
        val numberOfView: Long,
        val numberOfLike: Long,
    )

    /**
     * Use this to receive a string response
     * @return a structured output
     * */
    fun simplePrompt(query: String): String {
        return chatClient.prompt(query).call().content() ?: ""
    }

    /**
     * Use this to stuff the provided data into the prompt manually
     * */
    fun simplePromptStuffing(query: String): String {
        val prompt = PromptTemplate(
            mfvPromptResource,
            mapOf("query" to query, "docs" to mfvHealthCheckData.getContentAsString(Charset.defaultCharset()))
        ).create()

        return chatClient.prompt(prompt).call().content() ?: ""
    }

    /**
     * Use this to stuff the provided data into the prompt with simple vector strore
     * @see com.example.demo.configuration.RagConfiguration.simpleVectorStore
     * */
    fun promptStuffingWithSimpleVectorStore(query: String): String {
        val similarDocs = simpleVectorStore.similaritySearch(query)
        val contents = similarDocs.map { it.content }
        val prompt = PromptTemplate(
            mfvPromptResource,
            mapOf("query" to query, "docs" to contents.joinToString("\n"))
        ).create()

        return chatClient.prompt(prompt).call().content() ?: ""
    }

    /**
     * Use this to stuff the provided data into the prompt with simple vector strore
     * @see com.example.demo.dataloader.PdfLoader.load
     * */
    fun promptStuffingWithPgVectorStore(query: String): String {
        val similarDocs = pgVectorStore.similaritySearch(query)
        val contents = similarDocs.map { it.content }
        val prompt = PromptTemplate(
            mfvPromptResource,
            mapOf("query" to query, "docs" to contents.joinToString("\n"))
        ).create()

        return chatClient.prompt(prompt).call().content() ?: ""
    }

    /**
     * Use this to ask the MFV Employee list
     * @see com.example.demo.configuration.FunctionConfiguration
     * */
    fun functionCalling(query: String): String {
        val openAiChatOption = OpenAiChatOptions.builder()
            .withFunction("companyEmployee")
            .build()

        val prompt = PromptTemplate(mfvPromptResource).create(mapOf("query" to query), openAiChatOption)

        return chatClient.prompt(prompt).call().content() ?: ""
    }
}
