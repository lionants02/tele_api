package th.nstda.thongkum.tele_api.services.conference.join

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

/**
 * ตัว Object ใข้สำหรับโครงสร้างใน Database
 */
internal object JoinQueueExpose : Table() {
    val queue_code = varchar("queue_code", 30).uniqueIndex()
    val start_time = datetime("start_time")
    val end_time = datetime("end_time")
    val link_join = varchar("link_join", 255)
    val api_vdo = varchar("api_vdo", 100)
    val secret_vdo = varchar("secret_vdo", 100)
    val createAt = datetime("create_at")

    /**
     * ใช้สำหรับ แมพข้อมูลจาก record ใน database
     * ข้อมูลสำหรับคืนค่าไปยัง client จะตัดข้อมูลบางส่วนออก
     * สาเหตุที่ไม่ได้ใช้การ inheritance เพราะว่า ไม่มีเวลาทดสอบ
     * ตัดปัญหาโดยการ ระบุฟิวที่จะคืนค่าไปตั้งแต่ต้นเลย
     */
    fun mapResultResponse(result: ResultRow): JoinQueueResponse {
        val joinQueueData = JoinQueueData(
            result[queue_code],
            result[start_time],
            result[end_time]
        )
        return JoinQueueResponse(
            joinQueueData,
            result[createAt],
            result[link_join]
        )
    }

    /**
     * ใช้สำหรับ แมพข้อมูลจาก record ใน database
     * เป็นข้อมูลใช้สำหรับภายใน จะมีฟิวที่จำเป็นเพิ่มเข้ามา
     */
    fun mapResultSystem(result: ResultRow): JoinQueueSystemResponse {
        val joinQueueData = JoinQueueData(
            result[queue_code],
            result[start_time],
            result[end_time]
        )
        return JoinQueueSystemResponse(
            joinQueueData,
            result[createAt],
            result[link_join],
            result[api_vdo],
            result[secret_vdo]
        )
    }
}