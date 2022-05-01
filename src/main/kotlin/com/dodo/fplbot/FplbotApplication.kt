package com.dodo.fplbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableFeignClients
@EnableScheduling
class FplbotApplication

fun main(args: Array<String>) {
	runApplication<FplbotApplication>(*args)
}
