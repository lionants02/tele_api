package th.nstda.thongkum.tele_api.services.conference.join

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlin.time.DurationUnit

internal class Util {
    companion object {
        fun convertDataToData2(join: JoinQueueData): JoinQueue2Data {
            val instant = join.start_time.toInstant(TimeZone.UTC)
            val dateTimeString = instant.toString()
            val reserveDate = dateTimeString.substring(0, 10)
            val reserveTime = dateTimeString.substring(11, 19)
            val duration = (join.end_time.toInstant(TimeZone.UTC) - instant).toInt(DurationUnit.MINUTES)
            return JoinQueue2Data(join.queue_code, reserveDate, reserveTime, duration)
        }
    }
}