package com.dodo.fplbot.service

import mu.KLogging
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class CronService(
        private val contentService: ContentService
) {
    companion object : KLogging()

    @Scheduled(cron = "\${update.cron}")
    fun scheduleFixedDelayTask() {
        logger.info { "Event update is starting..." }
        val updatedContent = contentService.getContent()
        contentService.processMatchEvents(updatedContent)
        contentService.updateEventContent(updatedContent)
    }
}