package th.nstda.thongkum.tele_api.services.conference.join

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import th.nstda.thongkum.tele_api.config
import th.nstda.thongkum.tele_api.services.conference.db.HikariCPConnection
import th.nstda.thongkum.tele_api.services.conference.vdo.VdoServerController
import kotlin.time.Duration.Companion.hours

class JoinController : HikariCPConnection() {
    fun getTest(): JoinQueueData {
        val nowUTC = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        val nowBangkok = Clock.System.now().plus(7.hours).toLocalDateTime(TimeZone.UTC)
        return JoinQueueData("test", nowUTC, nowBangkok)
    }

    fun get(queueCode: String): JoinQueueResponse {
        return transaction {
            JoinQueueExpose.select(JoinQueueExpose.queue_code eq queueCode).limit(1).map {
                JoinQueueExpose.mapResultResponse(it)
            }.first()
        }
    }

    fun post(join: JoinQueueData): JoinQueueResponse {
        require(join.queue_code.isNotBlank()) { "queue code มีค่าว่าง" }
        val vdoServer = VdoServerController.instant.getServer()
        val createLinkJoin = "${config.frontEnd}/${join.queue_code}"
        transaction {
            SchemaUtils.create(JoinQueueExpose)
            JoinQueueExpose.insert {
                it[queue_code] = join.queue_code
                it[reservation_date] = join.reservation_date
                it[reservation_time] = join.reservation_time
                it[link_join] = createLinkJoin
                it[api_vdo] = vdoServer.api
                it[secret_vdo] = vdoServer.secret
                it[createAt] = Clock.System.now().toLocalDateTime(TimeZone.UTC)
            }
        }
        return get(join.queue_code)
    }

    companion object {
        val instant by lazy { JoinController() }
    }
}