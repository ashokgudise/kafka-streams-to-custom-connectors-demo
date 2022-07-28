package com.ashok.demos.kafkaconsumer.config

import com.ashok.demos.domain.Alert
import lombok.extern.slf4j.Slf4j
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Service


@Service
@Slf4j
class KafkaDealsConsumerConfig {



    @KafkaListener(topics = ["deals_topic"], groupId = "deal-avro-consumer")
    fun consume(record: ConsumerRecord<String?, Alert?>) {
        println(java.lang.String.format("Consumed message -> %s", record.value()))
    }

}