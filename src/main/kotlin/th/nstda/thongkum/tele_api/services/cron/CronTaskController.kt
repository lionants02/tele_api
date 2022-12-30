package th.nstda.thongkum.tele_api.services.cron

import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import th.nstda.thongkum.tele_api.getLogger
import th.nstda.thongkum.tele_api.services.conference.join.JoinController
import th.nstda.thongkum.tele_api.services.conference.vdo.VdoServerController
import th.nstda.thongkum.tele_api.services.conference.vdo.vidu.Vidu
import th.nstda.thongkum.tele_api.services.conference.vdo.vidu.ViduRest
import th.nstda.thongkum.tele_api.services.conference.vdo.vidu.ViduSecret

class CronTaskController : CronTask {

    private val log by lazy { getLogger(CronTaskController::class.java) }
    override fun run() {
        log.info("Run Cron")
        clearSession()
    }

    private fun clearSession() {
        val now = Clock.System.now().toLocalDateTime(TimeZone.UTC)
        log.info("Clear session now is $now")
        VdoServerController.instant.getServers().forEach { server ->
            val viduRest: Vidu = ViduRest(ViduSecret(server.api, server.secret))
            val sessionsOverTime = viduRest.getSessions().filter { sessionName ->
                try {
                    val sessionConfig = JoinController.instant.get(sessionName)
                    if (sessionConfig.property.start_time > now)
                        true
                    else sessionConfig.property.end_time < now
                } catch (ex: Exception) {
                    // if error not select
                    false
                }

            }

            sessionsOverTime.forEach { sessionName ->
                try {
                    viduRest.closeSession(sessionName)
                } catch (ex: Exception) {
                    getLogger(CronTaskController::class.java).warn("Cron Error ${ex.message}", ex)
                }
            }

        }
    }
}