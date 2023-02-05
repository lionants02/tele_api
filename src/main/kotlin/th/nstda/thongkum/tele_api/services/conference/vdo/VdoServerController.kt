package th.nstda.thongkum.tele_api.services.conference.vdo

import io.openvidu.java.client.OpenVidu
import io.openvidu.java.client.RecordingMode
import io.openvidu.java.client.SessionProperties
import kotlinx.datetime.Clock
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import th.nstda.thongkum.tele_api.config
import th.nstda.thongkum.tele_api.db.HikariCPConnection
import th.nstda.thongkum.tele_api.getLogger
import th.nstda.thongkum.tele_api.services.conference.join.JoinController
import th.nstda.thongkum.tele_api.services.conference.join.JoinQueueSystemResponse
import th.nstda.thongkum.tele_api.services.conference.vdo.vidu.ViduRest
import th.nstda.thongkum.tele_api.services.conference.vdo.vidu.ViduSecret
import kotlin.time.DurationUnit.SECONDS
import kotlin.time.toDuration

class VdoServerController : HikariCPConnection() {

    fun getServer(): VdoServerData {
        return Util.rndServerWithWeight(getServers())
    }

    fun getServers(): List<VdoServerData> {
        return transaction {
            SchemaUtils.create(VdoServerExpose)
            VdoServerExpose.selectAll().map {
                VdoServerExpose.mapResult(it)
            }.toList()
        }.toList()
    }

    /**
     * สร้างห้องประชุม vdo
     */
    fun creteSession(sessionName: String) {
        val check: JoinQueueSystemResponse = JoinController.instant.getSystemDetail(sessionName.trim())
        creteSession(sessionName.trim(), check)
    }

    fun creteSession(sessionName: String, check: JoinQueueSystemResponse) {
        val now = Clock.System.now()
            .plus(config.enterEarlySec.toDuration(SECONDS)) // เข้าก่อนเวลากี่วิ
            .toLocalDateTime(TimeZone.UTC)
        require(checkTime(check, now)) { "อยู่ขอบเขตนอกเวลาที่สร้างห้อง out of datetime" }

        val myVidu = ViduRest(ViduSecret(check.apiVdo, check.secretVdo))
        if (myVidu.haveSession(sessionName.trim())) {
            log.warn("มี session $sessionName อยู่ในระบบแล้ว have session $sessionName in system")
            return
        }

        val properties: SessionProperties = SessionProperties.Builder()
            .customSessionId(sessionName.trim())
            .recordingMode(RecordingMode.MANUAL)
            .build()
        val vidu = OpenVidu(check.apiVdo, check.secretVdo)
        val session = vidu.createSession(properties)

        log.info("Create session ${session.sessionId} create at ${session.createdAt()}")
    }

    fun createUserWebRTCToken(sessionName: String): String {
        val check = JoinController.instant.getSystemDetail(sessionName.trim())
        creteSession(sessionName.trim(), check)
        val myVidu = ViduRest(ViduSecret(check.apiVdo, check.secretVdo))
        return myVidu.getConnection(sessionName.trim(), "x")
    }

    /**
     * สามารถสร้างห้องตามช่วงเวลาได้ไหม
     */
    private fun checkTime(check: JoinQueueSystemResponse, now: LocalDateTime): Boolean {
        if (now > check.property.end_time)
            return false
        if (now < check.property.start_time)
            return false
        return true
    }

    companion object {
        val instant by lazy { VdoServerController() }
        private val log by lazy { getLogger(VdoServerController::class.java) }
    }
}