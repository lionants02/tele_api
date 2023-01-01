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
    val x_forwarded_for = varchar("x_forwarded_for", 50)
    val cf_ipcountry = varchar("cf_ipcountry", 50)
    val user_agent = text("user_agent")
    val referer = text("referer")

}