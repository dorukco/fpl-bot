package com.dodo.fplbot.model

import com.dodo.fplbot.client.BootstrapDto
import com.dodo.fplbot.client.ElementDto
import com.dodo.fplbot.client.EventDto
import com.dodo.fplbot.client.Identifier
import com.dodo.fplbot.client.StatDto
import com.dodo.fplbot.client.StatElementDto
import com.dodo.fplbot.client.TeamDto
import java.time.ZonedDateTime

data class StaticContent(
        val players: Map<Long?, Player>,
        val teams: Map<Long?, Team>
)

data class Player(
        val firstName: String?,
        val lastName: String?,
        val name: String?,
        val teamId: Int?
)

data class Team(
        val name: String?,
        val shortName: String?
)

data class EventKey(
        val code: Long?,
        val event: Int?,
)

data class Event(
        val kickOffTime: ZonedDateTime?,
        val finished: Boolean?,
        val finishedProvisional: Boolean?,
        val started: Boolean?,
        val homeTeam: String?,
        val homeTeamScore: Int?,
        val awayTeam: String?,
        val awayTeamScore: Int?,
        val stats: Map<Identifier?, Stat>?
)

data class Stat(
        val identifier: Identifier?,
        val homeTeamStats: List<StatElement>?,
        val awayTeamStats: List<StatElement>?
)

data class StatElement(
        val player: String?,
        val value: Int?
)

data class ChangedStats(
        val homeTeamStats: MutableList<ChangedStat>,
        val awayTeamStats: MutableList<ChangedStat>
)

data class ChangedStat(
        val team: String?,
        val action: Identifier?,
        val player: String?,
        val value: Int?
)

fun BootstrapDto?.toStaticContent() = this.let { dto ->
    StaticContent(
            players = dto?.elements?.associate { it.id to it.toPlayer() } ?: mapOf(),
            teams = dto?.teams?.associate { it.id to it.toTeam() } ?: mapOf()
    )
}

fun EventDto.toEvent(staticContent: StaticContent) = Event(
        kickOffTime = kickOffTime,
        finished = finished,
        finishedProvisional = finishedProvisional,
        started = started,
        homeTeam = staticContent.teams[homeTeam?.toLong()]?.name,
        awayTeam = staticContent.teams[awayTeam?.toLong()]?.name,
        homeTeamScore = homeTeamScore,
        awayTeamScore = awayTeamScore,
        stats = stats?.associate { it.identifier to it.toStat(staticContent.players) } ?: mapOf()
)

fun StatDto.toStat(players: Map<Long?, Player>) = Stat(
        identifier = identifier,
        homeTeamStats = homeTeamStats?.map { it.toStatElement(players) },
        awayTeamStats = awayTeamStats?.map { it.toStatElement(players) }
)

fun StatElementDto.toStatElement(players: Map<Long?, Player>) = StatElement(
        value = value,
        player = players[element?.toLong()]?.name
)

fun TeamDto.toTeam() = Team(
        name = name,
        shortName = shortName
)

fun ElementDto.toPlayer() = Player(
        firstName = firstName,
        lastName = secondName,
        name = webName,
        teamId = team
)

fun eventTemplate(header: String, event: Event?) = """
        |$header
        |${event?.homeTeam}: ${event?.homeTeamScore}
        |${event?.awayTeam}: ${event?.awayTeamScore}
    """.trimMargin()

fun statTemplate(changedStat: ChangedStat?) = """
        |${changedStat?.team}
        |Stat: ${changedStat?.action}
        |${changedStat?.player}: ${changedStat?.value}
    """.trimMargin()