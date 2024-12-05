package com.hopie.springaidemo.dataloader

import jakarta.annotation.PostConstruct
import org.slf4j.LoggerFactory
import org.springframework.ai.reader.pdf.PagePdfDocumentReader
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig
import org.springframework.ai.transformer.splitter.TokenTextSplitter
import org.springframework.ai.vectorstore.PgVectorStore
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Component

@Component
class PdfLoader(
    private val vectorStore: PgVectorStore,
    private val jdbcClient: JdbcClient
) {
    @Value("classpath:/data/mfv-annual-health-check-2024.pdf")
    private lateinit var pdfResource: Resource

    private val logger = LoggerFactory.getLogger(PdfLoader::class.java)

    @PostConstruct
    fun load() {
        val count = jdbcClient.sql("SELECT count(*) FROM vector_store")
            .query(Int::class.java)
            .single()

        if (count == 0) {
            logger.info("No vector store found in database")
            logger.info("Loading vector store from PDF: $pdfResource")
            val config = PdfDocumentReaderConfig.defaultConfig()
            val pdfReader = PagePdfDocumentReader(pdfResource, config)
            val documents = pdfReader.read()
            val splitDocuments = TokenTextSplitter().apply(documents)
            vectorStore.accept(splitDocuments)
        }

    }
}
