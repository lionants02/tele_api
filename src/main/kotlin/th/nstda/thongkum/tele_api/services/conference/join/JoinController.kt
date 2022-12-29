package th.nstda.thongkum.tele_api.services.conference.join

import io.ktor.server.plugins.*
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
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
        return JoinQueueResponse(
            inputData,
            Clock.System.now().plus(7.minutes).toLocalDateTime(TimeZone.UTC),
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
                    it[createAt] = Clock.System.now().toLocalDateTime(TimeZone.UTC)
                }
            }
        } catch (ex: org.jetbrains.exposed.exceptions.ExposedSQLException) {
            val message = ex.message
            if (message?.contains("duplicate key value violates unique constraint \"joinqueueexpose_queue_code_unique\"") == true) {
                print("Test error $message")
                throw BadRequestException("Create duplicate queue code")
            } else
                throw ex
        }
        return get(join.queue_code)
    }

    companion object {
        val instant by lazy { JoinController() }
    }
}