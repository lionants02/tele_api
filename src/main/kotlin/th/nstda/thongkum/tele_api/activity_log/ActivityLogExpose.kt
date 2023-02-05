package th.nstda.thongkum.tele_api.activity_log

import org.jetbrains.exposed.sql.Table
import org.jetbrains.exposed.sql.kotlin.datetime.timestamp

internal object ActivityLogExpose : Table("activity") {
    val time = timestamp("time")
    val type = varchar("type", 10)
    val message = text("message")
}