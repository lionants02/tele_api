package th.nstda.thongkum.tele_api.services.conference.join

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class JoinQueueResponse(
    val property: JoinQueueData,
    val createAt: LocalDateTime,
    var joinLink: String
)
