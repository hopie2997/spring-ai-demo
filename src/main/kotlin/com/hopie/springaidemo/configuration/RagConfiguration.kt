package com.hopie.springaidemo.configuration

import org.slf4j.LoggerFactory
import org.springframework.ai.embedding.EmbeddingModel
import org.springframework.ai.reader.TextReader
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.SimpleVectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.io.Resource
import java.nio.file.Paths

@Configuration
class RagConfiguration {
    @Value("classpath:/data/mfv-annual-health-check-2024.txt")
    private lateinit var mfvHealthCheckResource: Resource

    @Value("vectorstore.json")
    private lateinit var vectorStoreName: String

    private val logger = LoggerFactory.getLogger(RagConfiguration::class.java)

    @Bean
    fun simpleVectorStore(embedding: EmbeddingModel): SimpleVectorStore {
        val vectorStore = SimpleVectorStore(embedding)
        val vectorFile = Paths.get("src", "main", "resources", "data", vectorStoreName).toFile()

        if (vectorFile.exists()) {
            logger.info("Loading vector store from file: $vectorFile")
            vectorStore.load(vectorFile)
        } else {
            logger.info("Vector store file not found: ${vectorFile.path}")
            logger.info("Creating vector store from text file: $mfvHealthCheckResource")
            val documents = TextReader(mfvHealthCheckResource).read()
            val splitDocuments = TokenTextSplitter().apply(documents)
            vectorStore.add(splitDocuments)
            vectorStore.save(vectorFile)
        }

        return vectorStore
    }
}
