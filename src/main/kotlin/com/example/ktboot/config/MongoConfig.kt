package com.example.ktboot.config

import com.mongodb.*
import com.mongodb.reactivestreams.client.MongoClients
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.mongo.MongoProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.data.mongodb.ReactiveMongoDatabaseFactory
import org.springframework.data.mongodb.core.SimpleReactiveMongoDatabaseFactory
import java.util.concurrent.TimeUnit

private val logger = KotlinLogging.logger {}

@Configuration
@Profile("dev")
class MongoConfig {
    @Autowired
    private lateinit var mongoProperties: MongoProperties

    @Bean
    fun mongoClient(): ReactiveMongoDatabaseFactory {
        logger.info { "Configure MongoDB" }
        val settings = MongoClientSettings.builder()
            .applyConnectionString(ConnectionString(mongoProperties.uri))
            .applyToConnectionPoolSettings {
                it.minSize(1)
                    .maxSize(3)
                    .maxWaitQueueSize(3 * 7) // maxPoolSize * waitQueueMultiple
                    .maxWaitTime(1500, TimeUnit.MILLISECONDS)
                    .maxConnectionLifeTime(30 * 60 * 1000, TimeUnit.MILLISECONDS)
                    .maxConnectionIdleTime(25 * 60 * 1000, TimeUnit.MILLISECONDS)
            }
            .applyToSocketSettings {
                it.connectTimeout(2500, TimeUnit.MILLISECONDS)
                    .readTimeout(6000, TimeUnit.MILLISECONDS)
            }
            .writeConcern(WriteConcern.ACKNOWLEDGED)
            .compressorList(
                listOf(
                    MongoCompressor.createZlibCompressor()
                        .withProperty(MongoCompressor.LEVEL, 5)
                )
            )
            .retryWrites(true)
            .build()
        return SimpleReactiveMongoDatabaseFactory(
            MongoClients.create(settings), mongoProperties.database
        )
    }
}

