package th.nstda.thongkum.tele_api.services.conference.join

import kotlinx.datetime.LocalDateTime

data class JoinQueueSystemResponse(
    val property: JoinQueueData,
    val createAt: LocalDateTime,
    var joinLink: String,
    val apiVdo: String,
    val secretVdo: String
)
