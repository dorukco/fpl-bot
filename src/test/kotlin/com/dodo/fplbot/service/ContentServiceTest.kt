package com.dodo.fplbot.service

import com.dodo.fplbot.AWAY_TEAM
import com.dodo.fplbot.CURRENT_EVENT
import com.dodo.fplbot.EVENT_CODE
import com.dodo.fplbot.HOME_TEAM
import com.dodo.fplbot.MATCH_KICKOFF
import com.dodo.fplbot.bootstrapDto
import com.dodo.fplbot.client.FplEventClient
import com.dodo.fplbot.client.Identifier
import com.dodo.fplbot.eventContent
import com.dodo.fplbot.eventContent1
import com.dodo.fplbot.eventContent2
import com.dodo.fplbot.eventContent3
import com.dodo.fplbot.eventContent4
import com.dodo.fplbot.faultyEventContent
import com.dodo.fplbot.futureEventContent
import com.dodo.fplbot.model.ChangedStat
import com.dodo.fplbot.model.Event
import com.dodo.fplbot.model.EventKey
import com.dodo.fplbot.model.Stat
import com.dodo.fplbot.model.StatElement
import com.dodo.fplbot.model.eventTemplate
import com.dodo.fplbot.model.statTemplate
import com.dodo.fplbot.model.toEvent
import com.dodo.fplbot.model.toStaticContent
import com.dodo.fplbot.telegram.TelegramBot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.spyk
import io.mockk.verify
import io.mockk.verifySequence
import org.junit.jupiter.api.Test
import strikt.api.expectThat
import strikt.assertions.get
import strikt.assertions.hasSize
import strikt.assertions.isEmpty
import strikt.assertions.isEqualTo
import strikt.assertions.isNotNull

class ContentServiceTest {

    private val mockEventContent = spyk<MutableMap<EventKey, Event>>()
    private val fplEventClient = mockk<FplEventClient>()
    private val telegramBot = mockk<TelegramBot>(relaxed = true)

    private var contentService = ContentService(
            staticContent = bootstrapDto.toStaticContent(),
            eventContent = mockEventContent,
            fplEventClient = fplEventClient,
            telegramBot = telegramBot
    )

    @Test
    fun `get content successfully`() {
        every { fplEventClient.getFutureContent(1) } returns futureEventContent
        every { fplEventClient.getContent(CURRENT_EVENT) } returns eventContent

        val events = contentService.getContent()
        expectThat(events).isNotNull().hasSize(1).and {
            get { subject }[EventKey(EVENT_CODE, CURRENT_EVENT)].isNotNull().and {
                get { homeTeam } isEqualTo HOME_TEAM
                get { homeTeamScore } isEqualTo 0
                get { awayTeam } isEqualTo AWAY_TEAM
                get { awayTeamScore } isEqualTo 0
                get { started } isEqualTo true
                get { finished } isEqualTo false
                get { finishedProvisional } isEqualTo false
                get { kickOffTime } isEqualTo MATCH_KICKOFF
                get { stats }.isNotNull().isEmpty()
            }
        }
    }

    @Test
    fun `get content unsuccessfully`() {
        every { fplEventClient.getFutureContent(1) } throws Exception("Game is being processed.")
        val events = contentService.getContent()
        expectThat(events).isNotNull().isEmpty()
    }

    @Test
    fun `update event content`() {
        every { fplEventClient.getFutureContent(1) } returns futureEventContent
        every { fplEventClient.getContent(CURRENT_EVENT) } returns eventContent1
        val events = contentService.getContent()

        contentService.updateEventContent(events)
        verify(exactly = 1) { mockEventContent.clear() }

        val eventKeySlot = slot<EventKey>()
        val eventSlot = slot<Event>()
        verify { mockEventContent[capture(eventKeySlot)] = capture(eventSlot) }
        expectThat(eventKeySlot.captured) {
            get { code } isEqualTo EVENT_CODE
            get { event } isEqualTo CURRENT_EVENT
        }
        expectThat(eventSlot.captured) {
            get { homeTeam } isEqualTo HOME_TEAM
            get { homeTeamScore } isEqualTo 1
            get { awayTeam } isEqualTo AWAY_TEAM
            get { awayTeamScore } isEqualTo 1
            get { started } isEqualTo true
            get { finished } isEqualTo false
            get { finishedProvisional } isEqualTo false
            get { kickOffTime } isEqualTo MATCH_KICKOFF
            get { stats }.isNotNull() isEqualTo mapOf(
                    Identifier.GOALS_SCORED to Stat(
                            identifier = Identifier.GOALS_SCORED,
                            homeTeamStats = listOf(
                                    StatElement(
                                            player = "Salah",
                                            value = 1
                                    )
                            ),
                            awayTeamStats = listOf(
                                    StatElement(
                                            player = "Grealish",
                                            value = 1
                                    )
                            )
                    ),
                    Identifier.YELLOW_CARDS to Stat(
                            identifier = Identifier.YELLOW_CARDS,
                            homeTeamStats = listOf(
                                    StatElement(
                                            player = "van Dijk",
                                            value = 1
                                    )
                            ),
                            awayTeamStats = listOf(
                                    StatElement(
                                            player = "Dias",
                                            value = 1
                                    )
                            )
                    )
            )
        }
    }

    @Test
    fun `process match with no events`() {
        every { fplEventClient.getFutureContent(1) } returns futureEventContent
        every { fplEventClient.getContent(CURRENT_EVENT) } returns eventContent
        every { mockEventContent[EventKey(EVENT_CODE, CURRENT_EVENT)] } returns eventContent.map { it.toEvent(bootstrapDto.toStaticContent()) }.first()
        val events = contentService.getContent()

        contentService.processMatchEvents(events)
        verify(exactly = 0) { telegramBot.sendMessage(any()) }
    }

    @Test
    fun `process match with events`() {
        every { fplEventClient.getFutureContent(1) } returns futureEventContent
        every { fplEventClient.getContent(CURRENT_EVENT) } returns eventContent1
        every { mockEventContent[EventKey(EVENT_CODE, CURRENT_EVENT)] } returns eventContent.map { it.toEvent(bootstrapDto.toStaticContent()) }.first()
        val events = contentService.getContent()

        contentService.processMatchEvents(events)
        verify(exactly = 5) {
            telegramBot.sendMessage(any())
        }
        verifySequence {
            telegramBot.sendMessage(eventTemplate("Goal", eventContent1.map { it.toEvent(bootstrapDto.toStaticContent()) }.first()))
            telegramBot.sendMessage(statTemplate(ChangedStat("Liverpool", Identifier.GOALS_SCORED, "Salah", 1)))
            telegramBot.sendMessage(statTemplate(ChangedStat("Liverpool", Identifier.YELLOW_CARDS, "van Dijk", 1)))
            telegramBot.sendMessage(statTemplate(ChangedStat("Man City", Identifier.GOALS_SCORED, "Grealish", 1)))
            telegramBot.sendMessage(statTemplate(ChangedStat("Man City", Identifier.YELLOW_CARDS, "Dias", 1)))
        }
    }

    @Test
    fun `process match with more events`() {
        every { fplEventClient.getFutureContent(1) } returns futureEventContent
        every { fplEventClient.getContent(CURRENT_EVENT) } returns eventContent2
        every { mockEventContent[EventKey(EVENT_CODE, CURRENT_EVENT)] } returns eventContent1.map { it.toEvent(bootstrapDto.toStaticContent()) }.first()
        val events = contentService.getContent()

        contentService.processMatchEvents(events)
        verify(exactly = 5) {
            telegramBot.sendMessage(any())
        }
        verifySequence {
            telegramBot.sendMessage(eventTemplate("Goal", eventContent2.map { it.toEvent(bootstrapDto.toStaticContent()) }.first()))
            telegramBot.sendMessage(statTemplate(ChangedStat("Liverpool", Identifier.GOALS_SCORED, "Mané", 1)))
            telegramBot.sendMessage(statTemplate(ChangedStat("Man City", Identifier.GOALS_SCORED, "Sterling", 2)))
            telegramBot.sendMessage(statTemplate(ChangedStat("Man City", Identifier.YELLOW_CARDS, "Dias", 2)))
            telegramBot.sendMessage(statTemplate(ChangedStat("Man City", Identifier.RED_CARD, "Dias", 1)))
        }
    }

    @Test
    fun `process finished match`() {
        every { fplEventClient.getFutureContent(1) } returns futureEventContent
        every { fplEventClient.getContent(CURRENT_EVENT) } returns eventContent3
        every { mockEventContent[EventKey(EVENT_CODE, CURRENT_EVENT)] } returns eventContent2.map { it.toEvent(bootstrapDto.toStaticContent()) }.first()
        val events = contentService.getContent()

        contentService.processMatchEvents(events)
        verify(exactly = 1) {
            telegramBot.sendMessage(any())
        }
        verifySequence {
            telegramBot.sendMessage(eventTemplate("Match score", eventContent3.map { it.toEvent(bootstrapDto.toStaticContent()) }.first()))
        }
    }

    @Test
    fun `process match with bonus events`() {
        every { fplEventClient.getFutureContent(1) } returns futureEventContent
        every { fplEventClient.getContent(CURRENT_EVENT) } returns eventContent4
        every { mockEventContent[EventKey(EVENT_CODE, CURRENT_EVENT)] } returns eventContent3.map { it.toEvent(bootstrapDto.toStaticContent()) }.first()
        val events = contentService.getContent()

        contentService.processMatchEvents(events)
        verify(exactly = 3) {
            telegramBot.sendMessage(any())
        }
        verifySequence {
            telegramBot.sendMessage(statTemplate(ChangedStat("Liverpool", Identifier.BONUS, "Salah", 1)))
            telegramBot.sendMessage(statTemplate(ChangedStat("Liverpool", Identifier.BONUS, "Mané", 2)))
            telegramBot.sendMessage(statTemplate(ChangedStat("Man City", Identifier.BONUS, "Sterling", 3)))
        }
    }

    @Test
    fun `process match with faulty event`() {
        every { fplEventClient.getFutureContent(1) } returns futureEventContent
        every { fplEventClient.getContent(CURRENT_EVENT) } returns faultyEventContent
        every { mockEventContent[EventKey(EVENT_CODE, CURRENT_EVENT)] } returns eventContent4.map { it.toEvent(bootstrapDto.toStaticContent()) }.first()
        val events = contentService.getContent()

        contentService.processMatchEvents(events)
        verify(exactly = 0) {
            telegramBot.sendMessage(any())
        }
    }
}