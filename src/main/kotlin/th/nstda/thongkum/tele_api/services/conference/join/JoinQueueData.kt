package th.nstda.thongkum.tele_api.services.conference.join

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

@Serializable
data class JoinQueueData(
    var queue_code: String,
    var reservation_date: LocalDateTime,
    var reservation_time: LocalDateTime
)