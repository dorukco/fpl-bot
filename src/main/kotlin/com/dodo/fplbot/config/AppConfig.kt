package com.dodo.fplbot.config

import com.dodo.fplbot.client.FplStaticClient
import com.dodo.fplbot.model.Event
import com.dodo.fplbot.model.EventKey
import com.dodo.fplbot.model.StaticContent
import com.dodo.fplbot.model.toStaticContent
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig(
        private val fplStaticClient: FplStaticClient
) {
    @Bean
    fun staticContent(): StaticContent = fplStaticClient.getContent().toStaticContent()

    @Bean
    fun eventContent(): MutableMap<EventKey, Event> = mutableMapOf()
}