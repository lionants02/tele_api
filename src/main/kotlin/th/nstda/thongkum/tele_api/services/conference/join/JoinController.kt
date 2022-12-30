package th.nstda.thongkum.tele_api.services.conference.join

import io.ktor.server.plugins.*
import kotlinx.datetime.*
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import th.nstda.thongkum.tele_api.config
import th.nstda.thongkum.tele_api.db.HikariCPConnection
import th.nstda.thongkum.tele_api.services.conference.vdo.VdoServerController
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

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
            inputData,
            now,
            now,
            "https://examle.hii.in.th/${inputData.queue_code}"
        )
    }

    /**
     * ดึงข้อมูล queue code
     */
    fun get(queueCode: String): JoinQueueResponse {
        return transaction {
            JoinQueueExpose.select(JoinQueueExpose.queue_code eq queueCode).limit(1).map {
                JoinQueueExpose.mapResultResponse(it)
            }.first()
        }
    }

    fun getSystemDetail(queueCode: String): JoinQueueSystemResponse {
        return transaction {
            JoinQueueExpose.select(JoinQueueExpose.queue_code eq queueCode).limit(1).map {
                JoinQueueExpose.mapResultSystem(it)
            }.first()
        }
    }

    /**
     * ข้อมูลสำหรับการจองใช้ vdo conference
     */
    fun post(join: JoinQueueData): JoinQueueResponse {
        require(join.queue_code.isNotBlank()) { "queue code มีค่าว่าง" }
        require(join.end_time >= join.start_time) { "Require end_time > start_time." }
        val now = getNowUTC()
        require(join.end_time >= now) { "Cannot crete queue_code ${join.queue_code} over end_time. now:$now <= ${join.end_time}" }

        val vdoServer = VdoServerController.instant.getServer()
        val createLinkJoin = "${config.frontEnd}?t=${join.queue_code}"
        try {
            transaction {
                SchemaUtils.create(JoinQueueExpose)
                JoinQueueExpose.insert {
                    it[queue_code] = join.queue_code
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
            } else
                throw ex
        }
        return get(join.queue_code)
    }

    fun update(queueCode: String, join: JoinQueueData): JoinQueueResponse {
        require(queueCode == join.queue_code) { "ref queue_code != queue_code $queueCode != ${join.queue_code}" }
        require(join.end_time >= join.start_time) { "Require end_time > start_time." }
        val oldJoin = get(queueCode)
        val now = getNowUTC()
        val oldEndTime = oldJoin.property.end_time
        require(oldEndTime >= now) { "Cannot update queue_code $queueCode old object over end_time. now:$now <= old end_time:$oldEndTime" }
        val vdoServer = VdoServerController.instant.getServer()
        val createLinkJoin = "${config.frontEnd}?t=${join.queue_code}"
        try {
            transaction {
                JoinQueueExpose.update({ JoinQueueExpose.queue_code eq queueCode }) {
                    it[queue_code] = join.queue_code
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
        return get(join.queue_code)
    }

    private fun getNowTimestampUTC(): Instant {
        return getNowUTC().toInstant(TimeZone.UTC)
    }

    private fun getNowUTC() = Clock.System.now().toLocalDateTime(TimeZone.UTC)

    companion object {
        val instant by lazy { JoinController() }
    }
}