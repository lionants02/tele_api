package th.nstda.thongkum.tele_api.services.conference.join

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable

/**
 * ข้อมูลใช้ตอน response ระบบห้อง vdo
 */
@Serializable
data class JoinQueue2Response(
    val property: JoinQueue2Data,
    val createAt: LocalDateTime,
    val updateAt: LocalDateTime,
    var joinLink: String
)