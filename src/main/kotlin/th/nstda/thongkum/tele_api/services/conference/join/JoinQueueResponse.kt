package th.nstda.thongkum.tele_api.services.conference.join

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * ข้อมูลใช้ตอน response ระบบห้อง vdo
 */
@Serializable
data class JoinQueueResponse(
    val property: JoinQueueData,
    val createAt: LocalDateTime,
    var joinLink: String
)
