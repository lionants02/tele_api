package th.nstda.thongkum.tele_api.activity_log

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object ActivityQueueExpose : Table("activity_queue") {
    val time = timestamp("time")
    val queue_code = varchar("queue_code", 50)

    /**
     * CREATE, UPDATE, DELETE
     */
    val activity = varchar("activity", 10)
    val message = text("message")

}