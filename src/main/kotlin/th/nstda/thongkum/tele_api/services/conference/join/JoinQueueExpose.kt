package th.nstda.thongkum.tele_api.services.conference.join

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.datetime

object JoinQueueExpose : Table() {
    val queue_code = varchar("queue_code", 30).uniqueIndex()
    val reservation_date = datetime("reservation_date")
    val reservation_time = datetime("reservation_time")
    val link_join = varchar("link_join", 255)
    val api_vdo = varchar("api_vdo", 100)
    val secret_vdo = varchar("secret_vdo", 100)
    val createAt = datetime("create_at")
    fun mapResultResponse(result: ResultRow): JoinQueueResponse {
        val joinQueueData = JoinQueueData(
            result[queue_code],
            result[reservation_date],
            result[reservation_time]
        )
        return JoinQueueResponse(
            joinQueueData,
            result[createAt],
            result[link_join]
        )
    }

    fun mapResultSystem(result: ResultRow): JoinQueueSystemResponse {
        val joinQueueData = JoinQueueData(
            result[queue_code],
            result[reservation_date],
            result[reservation_time]
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