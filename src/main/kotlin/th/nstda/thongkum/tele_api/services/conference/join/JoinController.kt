package th.nstda.thongkum.tele_api.services.conference.join

import io.ktor.server.plugins.*
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.transaction
import th.nstda.thongkum.tele_api.config
import th.nstda.thongkum.tele_api.db.HikariCPConnection
import th.nstda.thongkum.tele_api.services.conference.join.Util.Companion.convertDataToData2
import th.nstda.thongkum.tele_api.services.conference.vdo.VdoServerController
import java.util.UUID
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class JoinController : HikariCPConnection() {
    /**
     * ตัวอย่างรูปแบบข้อมูล ตอน input
     */
    fun getTest(): JoinQueueData {
        val nowUTC = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val nowBangkok = Clock.System.now().plus(7.hours).toLocalDateTime(TimeZone.UTC)
        return JoinQueueData("test", nowUTC, nowBangkok)
    }

    /**
     * ตัวอย่างรูปแบบข้อมูล ตอน response
     */
    fun getResponseTest(): JoinQueueResponse {
        val inputData = getTest()
        val now = Clock.System.now().plus(7.minutes).toLocalDateTime(TimeZone.UTC)
        return JoinQueueResponse(
            inputData, now, now, "https://examle.hii.in.th/${inputData.queue_code}"
        )
    }

    /**
     * ดึงข้อมูล queue code
     */
    fun get(queueCode: String): JoinQueueResponse {
        return transaction {
            JoinQueueExpose.select(JoinQueueExpose.queue_code eq queueCode.trim()).limit(1).map {
                JoinQueueExpose.mapResultResponse(it)
            }.first()
        }
    }

    fun getSystemDetail(queueCode: String): JoinQueueSystemResponse {
        return transaction {
            JoinQueueExpose.select(JoinQueueExpose.queue_code eq queueCode.trim()).limit(1).map {
                JoinQueueExpose.mapResultSystem(it)
            }.first()
        }
    }

    enum class CreateStatus {
        /**
         * มีข้อมูลเดิมอยู่แล้ว ส่งค่าเดิมกลับ
         * ไม่สร้างข้อมูลใหม่
         */
        FOUND_OLD_OBJECT,

        /**
         * ไม่มีข้อมูลเดิมอยู่
         * ทำการสร้างข้อมูลใหม่
         */
        CREATE_NEW_OBJECT
    }

    fun postV2(join: JoinQueue2Data): Pair<JoinQueue2Response, CreateStatus> {
        require(join.queue_code.trim().isNotEmpty()) { "queue_code is empty" }
        require(join.reserve_date.isNotEmpty()) { "reserve_date is empty" }
        require(join.reserve_time.isNotEmpty()) { "reserve_time is empty" }
        require(UUID.fromString(join.queue_code) != null) { "${join.queue_code} convert to UUID Error" }
        require(join.duration > 0) { "duration is ${join.duration} > 0" }
        return try {
            get(join.queue_code.trim()).let {
                JoinQueue2Response(
                    convertDataToData2(it.property),
                    it.createAt,
                    it.updateAt,
                    it.joinLink
                ) to CreateStatus.FOUND_OLD_OBJECT
            }
        } catch (ex: Exception) {
            val instant = "${join.reserve_date}T${join.reserve_time}.000Z".toInstant()
            val startTime = instant.toLocalDateTime(TimeZone.UTC)
            val endTime = instant.plus(join.duration.toDuration(DurationUnit.MINUTES)).toLocalDateTime(TimeZone.UTC)
            val createJoin = JoinQueueData(join.queue_code.trim(), startTime, endTime)
            val result = post(createJoin)
            JoinQueue2Response(
                convertDataToData2(result.property),
                result.createAt,
                result.updateAt,
                result.joinLink
            ) to CreateStatus.CREATE_NEW_OBJECT
        }
    }


    /**
     * ข้อมูลสำหรับการจองใช้ vdo conference
     */
    fun post(join: JoinQueueData): JoinQueueResponse {
        require(join.queue_code.trim().isNotBlank()) { "queue code (มีค่าว่าง)" }
        require(join.end_time >= join.start_time) { "Require end_time > start_time." }
        val now = getNowUTC()
        require(join.end_time >= now) {
            "Cannot crete queue_code ${join.queue_code} over end_time. now:$now <= ${join.end_time} " +
                    "ไม่สามารถจอง queue ได้เนื่องจาก เวลาสิ้นสุดของห้อง เป็นเวลาอดีต"
        }

        val vdoServer = VdoServerController.instant.getServer()
        val createLinkJoin = "${config.frontEnd}?t=${join.queue_code.trim()}&name=x"
        try {
            transaction {
                SchemaUtils.create(JoinQueueExpose)
                JoinQueueExpose.insert {
                    it[queue_code] = join.queue_code.trim()
                    it[start_time] = join.start_time
                    it[end_time] = join.end_time
                    it[link_join] = createLinkJoin
                    it[api_vdo] = vdoServer.api
                    it[secret_vdo] = vdoServer.secret
                    it[createAt] = getNowTimestampUTC()
                    it[updateAt] = getNowTimestampUTC()
                }
            }
        } catch (ex: org.jetbrains.exposed.exceptions.ExposedSQLException) {
            val message = ex.message
            if (message?.contains("duplicate key value violates unique constraint") == true) {
                throw BadRequestException("Create duplicate queue code")
            } else throw ex
        }
        return get(join.queue_code.trim())
    }

    fun update(queueCode: String, join: JoinQueueData): JoinQueueResponse {
        require(queueCode.trim().isNotEmpty()) { "queue_code ห้ามมีค่าว่าง" }
        require(join.queue_code.trim().isNotEmpty()) { "queue_code ใน body json ห้ามมีค่าว่าง" }
        require(queueCode.trim() == join.queue_code.trim()) { "ref queue_code != queue_code $queueCode != ${join.queue_code}" }
        require(join.end_time >= join.start_time) { "Require end_time > start_time." }
        val oldJoin = get(queueCode.trim())
        val now = getNowUTC()
        val oldEndTime = oldJoin.property.end_time
        require(oldEndTime >= now) { "Cannot update queue_code $queueCode old object over end_time. now:$now <= old end_time:$oldEndTime" }
        val vdoServer = VdoServerController.instant.getServer()
        val createLinkJoin = "${config.frontEnd}?t=${join.queue_code}"
        try {
            transaction {
                JoinQueueExpose.update({ JoinQueueExpose.queue_code eq queueCode.trim() }) {
                    it[queue_code] = join.queue_code.trim()
                    it[start_time] = join.start_time
                    it[end_time] = join.end_time
                    it[link_join] = createLinkJoin
                    it[api_vdo] = vdoServer.api
                    it[secret_vdo] = vdoServer.secret
                    it[updateAt] = getNowTimestampUTC()
                }
            }

        } catch (ex: Exception) {
            throw ex
        }
        return get(join.queue_code.trim())
    }

    private fun getNowTimestampUTC(): Instant {
        return getNowUTC().toInstant(TimeZone.UTC)
    }

    private fun getNowUTC() = Clock.System.now().toLocalDateTime(TimeZone.UTC)

    companion object {
        val instant by lazy { JoinController() }
    }
}