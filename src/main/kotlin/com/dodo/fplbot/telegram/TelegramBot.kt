package com.dodo.fplbot.telegram

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.runBlocking
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.telegram.telegrambots.bots.DefaultAbsSender
import org.telegram.telegrambots.bots.DefaultBotOptions
import org.telegram.telegrambots.meta.api.methods.send.SendMessage

@Component
class TelegramBot(
        @Value("#{'\${fplbot.telegram.chat.ids}'.split(',')}") private val cids: List<String>,
        @Value("\${fplbot.telegram.token}") private val token: String
) : DefaultAbsSender(DefaultBotOptions()) {

    companion object : KLogging()

    override fun getBotToken(): String {
        return token
    }

    fun sendMessage(message: String) {
        runBlocking(Dispatchers.IO) {
            cids.filterNot { it.isBlank() }.map { cid ->
                async {
                    runCatching {
                        val sent = SendMessage().apply {
                            chatId = cid
                            text = message
                        }
                        execute(sent)
                    }.onFailure {
                        logger.error { it }
                    }
                }
            }.awaitAll()
        }
    }
}