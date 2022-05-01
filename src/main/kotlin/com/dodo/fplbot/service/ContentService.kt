package com.dodo.fplbot.service

import com.dodo.fplbot.client.FplEventClient
import com.dodo.fplbot.client.Identifier
import com.dodo.fplbot.model.ChangedStat
import com.dodo.fplbot.model.ChangedStats
import com.dodo.fplbot.model.Event
import com.dodo.fplbot.model.EventKey
import com.dodo.fplbot.model.StaticContent
import com.dodo.fplbot.model.eventTemplate
import com.dodo.fplbot.model.statTemplate
import com.dodo.fplbot.model.toEvent
import com.dodo.fplbot.telegram.TelegramBot
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class ContentService(
        private val staticContent: StaticContent,
        private val eventContent: MutableMap<EventKey, Event>,
        private val fplEventClient: FplEventClient,
        private val telegramBot: TelegramBot
) {

    companion object : KLogging()

    private val notifExcludes = listOf(Identifier.BPS, Identifier.SAVES)

    fun getContent(): Map<EventKey, Event>? = runCatching {
        fplEventClient.getFutureContent(1).let { futureDtos ->
            val futureEvent = futureDtos?.first()?.event
            val currentEvent = if (futureEvent != null) futureEvent - 1 else return mapOf()
            fplEventClient.getContent(currentEvent).let { dtos ->
                dtos?.map { EventKey(it.code, it.event) to it.toEvent(staticContent) }
            }?.toMap()
        }
    }.getOrElse {
        logger.error(it) { "Event content cannot be fetched." }
        mapOf()
    }

    fun updateEventContent(updatedContent: Map<EventKey, Event>?) {
        eventContent.clear()
        updatedContent?.forEach {
            eventContent[it.key] = it.value
        }
        logger.info { "Event content size: ${eventContent.size}" }
    }

    fun processMatchEvents(updatedContent: Map<EventKey, Event>?) {
        updatedContent?.forEach { updatedEvent ->
            val event = eventContent[updatedEvent.key] ?: return
            // Match started
            if (event.started == false && updatedEvent.value.started == true) {
                logger.info { eventTemplate("Kick off", updatedEvent.value) }
                telegramBot.sendMessage(eventTemplate("Kick off", updatedEvent.value))
            }
            // Score update
            val oldScore = "${event.homeTeamScore} - ${event.awayTeamScore}"
            val newScore = "${updatedEvent.value.homeTeamScore} - ${updatedEvent.value.awayTeamScore}"
            if (updatedEvent.value.started == true && oldScore != newScore) {
                logger.info { eventTemplate("Goal", updatedEvent.value) }
                telegramBot.sendMessage(eventTemplate("Goal", updatedEvent.value))
            }
            // Match stats
            val changedStats = getChangedStats(event, updatedEvent.value)
            changedStats.homeTeamStats.forEach {
                statTemplate(it).run {
                    logger.info { this }
                    telegramBot.sendMessage(this)
                }
            }
            changedStats.awayTeamStats.forEach {
                statTemplate(it).run {
                    logger.info { this }
                    telegramBot.sendMessage(this)
                }
            }
            // Match score
            if (event.finishedProvisional == false && updatedEvent.value.finishedProvisional == true) {
                logger.info { eventTemplate("Match score", updatedEvent.value) }
                telegramBot.sendMessage(eventTemplate("Match score", updatedEvent.value))
            }
        }
    }

    fun getChangedStats(oldEvent: Event?, newEvent: Event?): ChangedStats {
        val changedStats = ChangedStats(mutableListOf(), mutableListOf())
        Identifier.values().filterNot { notifExcludes.contains(it) }.forEach { identifier ->
            val newStats = newEvent?.stats?.get(identifier)
            val oldStats = oldEvent?.stats?.get(identifier)
            newStats?.homeTeamStats?.filterNot {
                oldStats?.homeTeamStats?.contains(it) ?: false
            }?.forEach {
                changedStats.homeTeamStats.add(ChangedStat(newEvent.homeTeam, identifier, it.player, it.value))
            }
            newStats?.awayTeamStats?.filterNot {
                oldStats?.awayTeamStats?.contains(it) ?: false
            }?.forEach {
                changedStats.awayTeamStats.add(ChangedStat(newEvent.awayTeam, identifier, it.player, it.value))
            }
        }
        return changedStats
    }
}