package com.ashok.demos.producer.config

import com.ashok.demos.domain.Alert
import io.github.serpro69.kfaker.faker
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.kafka.KafkaProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.integration.dsl.*
import org.springframework.integration.kafka.outbound.KafkaProducerMessageHandler
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory
import org.springframework.messaging.MessageHandler
import org.springframework.messaging.support.GenericMessage


@Profile("string")
@Configuration
class DealsIntegrationStringConfig {

    @Autowired
    lateinit var kafkaProperties: KafkaProperties

    @Bean
    fun producerFactory(): ProducerFactory<String, String> {
        val configProps: MutableMap<String, Any> = HashMap()
        configProps.putAll(kafkaProperties.buildProducerProperties())
        return DefaultKafkaProducerFactory<String, String>(configProps)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String>?{
        return KafkaTemplate(producerFactory())
    }

    @Bean
    fun kafkaProducerHandler(): MessageHandler? {
        val handler: KafkaProducerMessageHandler<String, String>
        = KafkaProducerMessageHandler(kafkaTemplate())
        handler.kafkaTemplate.defaultTopic =  "deals_topic"
        return handler
    }

    @Bean
    fun writeToKafka() = integrationFlow {
        kafkaProducerHandler()?.let { handle(it) }
    }

    @Bean
    fun integrationFlow(): IntegrationFlow? {
        return IntegrationFlows
            .from({ GenericMessage("")} ) {
            c: SourcePollingChannelAdapterSpec ->
            c.poller( Pollers.fixedDelay(1000))
                .autoStartup(true)
                .id("schedulerProducerFlow")
        }
            .transform<String, String> {
                val faker = faker { }
                val dealContributorName = faker.name.firstName()
                println("Deal alert from String Profile \t$dealContributorName")
                dealContributorName
            }
            .to(writeToKafka())
    }

}