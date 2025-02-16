package com.dodo.fplbot.client

import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.annotation.JsonValue
import java.time.ZonedDateTime

data class BootstrapDto(
        val elements: List<ElementDto>,
        val teams: List<TeamDto>
)

data class ElementDto(
        val id: Long?,
        @JsonProperty("first_name")
        val firstName: String?,
        @JsonProperty("second_name")
        val secondName: String?,
        @JsonProperty("web_name")
        val webName: String?,
        val team: Int?
)

data class TeamDto(
        val id: Long?,
        val name: String?,
        @JsonProperty("short_name")
        val shortName: String?
)

data class EventDto(
        val code: Long?,
        val event: Int?,
        @JsonProperty("kickoff_time")
        val kickOffTime: ZonedDateTime?,
        val finished: Boolean?,
        @JsonProperty("finished_provisional")
        val finishedProvisional: Boolean?,
        val started: Boolean?,
        @JsonProperty("team_h")
        val homeTeam: Int?,
        @JsonProperty("team_h_score")
        val homeTeamScore: Int = 0,
        @JsonProperty("team_a")
        val awayTeam: Int?,
        @JsonProperty("team_a_score")
        val awayTeamScore: Int = 0,
        val stats: List<StatDto>?
)

data class StatDto(
        val identifier: Identifier?,
        @JsonProperty("h")
        val homeTeamStats: List<StatElementDto>?,
        @JsonProperty("a")
        val awayTeamStats: List<StatElementDto>?
)

data class StatElementDto(
        val value: Int?,
        val element: Int?
)

data class GameDto(
        @JsonProperty("current_event")
        val currentEvent: Int?,
        @JsonProperty("current_event_finished")
        val currentEventFinished: Boolean?,
        @JsonProperty("waivers_processed")
        val waiversProcessed: Boolean?
)

enum class Identifier(@get:JsonValue val value: String) {
    GOALS_SCORED("goals_scored"),
    ASSISTS("assists"),
    OWN_GOALS("own_goals"),
    PENALTIES_SAVED("penalties_saved"),
    PENALTIES_MISSED("penalties_missed"),
    YELLOW_CARDS("yellow_cards"),
    RED_CARD("red_cards"),
    SAVES("saves"),
    BONUS("bonus"),
    BPS("bps"),
    MNG_WIN("mng_underdog_win"),
    MNG_DRAW("mng_underdog_draw")
}
