package com.dodo.fplbot

import com.dodo.fplbot.client.*
import java.time.ZonedDateTime

const val EVENT_CODE = 2210543L
const val CURRENT_EVENT = 27
const val HOME_TEAM = "Liverpool"
const val AWAY_TEAM = "Man City"
val MATCH_KICKOFF = ZonedDateTime.parse("2022-01-03T19:00:00+00:00")

val bootstrapDto = BootstrapDto(
        elements = listOf(
                ElementDto(
                        id = 1,
                        firstName = "Mohamed",
                        secondName = "Salah",
                        webName = "Salah",
                        team = 1
                ),
                ElementDto(
                        id = 2,
                        firstName = "Sadio",
                        secondName = "Mané",
                        webName = "Mané",
                        team = 1
                ),
                ElementDto(
                        id = 3,
                        firstName = "Virgil",
                        secondName = "van Dijk",
                        webName = "van Dijk",
                        team = 1
                ),
                ElementDto(
                        id = 4,
                        firstName = "Jack",
                        secondName = "Grealish",
                        webName = "Grealish",
                        team = 2
                ),
                ElementDto(
                        id = 5,
                        firstName = "Raheem",
                        secondName = "Sterling",
                        webName = "Sterling",
                        team = 2
                ),
                ElementDto(
                        id = 6,
                        firstName = "Rúben Santos",
                        secondName = "Gato Alves Dias",
                        webName = "Dias",
                        team = 2
                )
        ),
        teams = listOf(
                TeamDto(
                        id = 1,
                        name = HOME_TEAM,
                        shortName = "LIV"
                ),
                TeamDto(
                        id = 2,
                        name = AWAY_TEAM,
                        shortName = "MCI"
                )
        )
)

val gameContent = GameDto(
        currentEvent = CURRENT_EVENT,
        currentEventFinished = false,
        waiversProcessed = true
)

val faultyEventContent = listOf(
        EventDto(
                code = EVENT_CODE,
                event = null,
                kickOffTime = MATCH_KICKOFF,
                finished = false,
                finishedProvisional = false,
                started = false,
                homeTeam = null,
                homeTeamScore = 0,
                awayTeam = null,
                awayTeamScore = 0,
                stats = listOf()
        )
)

val eventContent = listOf(
        EventDto(
                code = EVENT_CODE,
                event = CURRENT_EVENT,
                kickOffTime = MATCH_KICKOFF,
                finished = false,
                finishedProvisional = false,
                started = true,
                homeTeam = 1,
                homeTeamScore = 0,
                awayTeam = 2,
                awayTeamScore = 0,
                stats = listOf()
        )
)

val eventContent1 = listOf(
        EventDto(
                code = EVENT_CODE,
                event = CURRENT_EVENT,
                kickOffTime = MATCH_KICKOFF,
                finished = false,
                finishedProvisional = false,
                started = true,
                homeTeam = 1,
                homeTeamScore = 1,
                awayTeam = 2,
                awayTeamScore = 1,
                stats = listOf(
                        StatDto(
                                identifier = Identifier.GOALS_SCORED,
                                homeTeamStats = listOf(
                                        StatElementDto(1, 1)
                                ),
                                awayTeamStats = listOf(
                                        StatElementDto(1, 4)
                                )
                        ),
                        StatDto(
                                identifier = Identifier.YELLOW_CARDS,
                                homeTeamStats = listOf(
                                        StatElementDto(1, 3)
                                ),
                                awayTeamStats = listOf(
                                        StatElementDto(1, 6)
                                )
                        )
                )
        )
)

val eventContent2 = listOf(
        EventDto(
                code = EVENT_CODE,
                event = CURRENT_EVENT,
                kickOffTime = MATCH_KICKOFF,
                finished = false,
                finishedProvisional = false,
                started = true,
                homeTeam = 1,
                homeTeamScore = 2,
                awayTeam = 2,
                awayTeamScore = 2,
                stats = listOf(
                        StatDto(
                                identifier = Identifier.GOALS_SCORED,
                                homeTeamStats = listOf(
                                        StatElementDto(1, 1),
                                        StatElementDto(1, 2)
                                ),
                                awayTeamStats = listOf(
                                        StatElementDto(2, 5)
                                )
                        ),
                        StatDto(
                                identifier = Identifier.YELLOW_CARDS,
                                homeTeamStats = listOf(
                                        StatElementDto(1, 3)
                                ),
                                awayTeamStats = listOf(
                                        StatElementDto(2, 6)
                                )
                        ),
                        StatDto(
                                identifier = Identifier.RED_CARD,
                                homeTeamStats = listOf(),
                                awayTeamStats = listOf(
                                        StatElementDto(1, 6)
                                )
                        )
                )
        )
)

val eventContent3 = listOf(
        EventDto(
                code = EVENT_CODE,
                event = CURRENT_EVENT,
                kickOffTime = MATCH_KICKOFF,
                finished = false,
                finishedProvisional = true,
                started = true,
                homeTeam = 1,
                homeTeamScore = 2,
                awayTeam = 2,
                awayTeamScore = 2,
                stats = listOf(
                        StatDto(
                                identifier = Identifier.GOALS_SCORED,
                                homeTeamStats = listOf(
                                        StatElementDto(1, 1),
                                        StatElementDto(1, 2)
                                ),
                                awayTeamStats = listOf(
                                        StatElementDto(2, 5)
                                )
                        ),
                        StatDto(
                                identifier = Identifier.YELLOW_CARDS,
                                homeTeamStats = listOf(
                                        StatElementDto(1, 3)
                                ),
                                awayTeamStats = listOf(
                                        StatElementDto(2, 6)
                                )
                        ),
                        StatDto(
                                identifier = Identifier.RED_CARD,
                                homeTeamStats = listOf(),
                                awayTeamStats = listOf(
                                        StatElementDto(1, 6)
                                )
                        )
                )
        )
)

val eventContent4 = listOf(
        EventDto(
                code = EVENT_CODE,
                event = CURRENT_EVENT,
                kickOffTime = MATCH_KICKOFF,
                finished = true,
                finishedProvisional = true,
                started = true,
                homeTeam = 1,
                homeTeamScore = 2,
                awayTeam = 2,
                awayTeamScore = 2,
                stats = listOf(
                        StatDto(
                                identifier = Identifier.GOALS_SCORED,
                                homeTeamStats = listOf(
                                        StatElementDto(1, 1),
                                        StatElementDto(1, 2)
                                ),
                                awayTeamStats = listOf(
                                        StatElementDto(2, 5)
                                )
                        ),
                        StatDto(
                                identifier = Identifier.YELLOW_CARDS,
                                homeTeamStats = listOf(
                                        StatElementDto(1, 3)
                                ),
                                awayTeamStats = listOf(
                                        StatElementDto(2, 6)
                                )
                        ),
                        StatDto(
                                identifier = Identifier.RED_CARD,
                                homeTeamStats = listOf(),
                                awayTeamStats = listOf(
                                        StatElementDto(1, 6)
                                )
                        ),
                        StatDto(
                                identifier = Identifier.BONUS,
                                homeTeamStats = listOf(
                                        StatElementDto(1, 1),
                                        StatElementDto(2, 2)
                                ),
                                awayTeamStats = listOf(
                                        StatElementDto(3, 5)
                                )
                        )
                )
        )
)
