package th.nstda.thongkum.tele_api.services.conference.join

import kotlinx.serialization.Serializable

/**
 * ข้อมูลใช้ตอน input ระบบจองห้อง vdo
 */
@Serializable
data class JoinQueue2Data(
    var queue_code: String,
    var reserve_date: String,
    var reserve_time: String,
    var duration: Int
)