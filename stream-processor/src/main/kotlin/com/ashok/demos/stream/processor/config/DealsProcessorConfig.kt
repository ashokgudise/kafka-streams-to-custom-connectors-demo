package com.ashok.demos.stream.processor.config

import com.ashok.demos.domain.Alert
import com.ashok.demos.domain.AlertSubscription
import io.github.serpro69.kfaker.faker
import lombok.extern.slf4j.Slf4j
import org.apache.kafka.streams.kstream.*
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
@Slf4j
class DealsProcessorConfig {

    @Bean
    fun dealsProcessor(): java.util.function.Function<KStream<String, Alert>, KStream<String, AlertSubscription>>? {
        return java.util.function.Function<KStream<String, Alert>, KStream<String, AlertSubscription>>
            { input -> input.mapValues { value ->
                    val fakerObj = faker{}
                    val alertSubscription = AlertSubscription( value.getName(), value.getType(), fakerObj.address.fullAddress(), fakerObj.phoneNumber.phoneNumber())
                    println("alert subscription generated $alertSubscription")
                    alertSubscription
                }
            }
    }
}