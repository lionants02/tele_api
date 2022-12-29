package th.nstda.thongkum.tele_api.activity_log

import kotlinx.datetime.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import th.nstda.thongkum.tele_api.db.HikariCPConnectionActivityLog

class ActivityLogController : ActivityLog, HikariCPConnectionActivityLog() {
    override fun log(type: ActivityLog.TYPE, message: () -> String) {
        transaction {
            SchemaUtils.create(ActivityLogExpose)
            ActivityLogExpose.insert {
                it[time] = getNowUTC()
                it[ActivityLogExpose.type] = type.name
                it[ActivityLogExpose.message] = message()
            }
        }
    }

    override fun logQueue(queueCode: String, activity: ActivityLog.ACTIVITY, message: () -> String) {
        transaction {
            SchemaUtils.create(ActivityQueueExpose)
            ActivityQueueExpose.insert {
                it[time] = getNowUTC()
                it[queue_code] = queueCode
                it[ActivityQueueExpose.activity] = activity.name
                it[ActivityQueueExpose.message] = message()
            }
        }
    }

    private fun getNowUTC(): Instant {
        return Clock.System.now().toLocalDateTime(TimeZone.UTC).toInstant(UtcOffset.ZERO)
    }

    companion object {
        val instant by lazy { ActivityLogController() }
    }
}