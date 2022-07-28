package com.ashok.demos.producer

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class AvroProducerApplication

fun main(args: Array<String>) {
    runApplication<AvroProducerApplication>(*args)
}
