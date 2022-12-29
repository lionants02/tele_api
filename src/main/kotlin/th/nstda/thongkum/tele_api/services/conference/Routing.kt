package th.nstda.thongkum.tele_api.services.conference

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import th.nstda.thongkum.tele_api.activity_log.ActivityLog.ACTIVITY.CREATE
import th.nstda.thongkum.tele_api.activity_log.ActivityLog.ACTIVITY.TOKEN
import th.nstda.thongkum.tele_api.activity_log.ActivityLogController
import th.nstda.thongkum.tele_api.config
import th.nstda.thongkum.tele_api.getLogger
import th.nstda.thongkum.tele_api.services.conference.join.JoinController
import th.nstda.thongkum.tele_api.services.conference.join.JoinQueueData
import th.nstda.thongkum.tele_api.services.conference.vdo.VdoServerController

fun Application.configureVdoRouting() {

    routing {
        /**
         * Join
         */
        post("/join/queue") {
            require(call.request.header("api-key") == config.apiKey) { "API KEY Not cCC" }
            val joinQueue = call.receive<JoinQueueData>()
            val create = JoinController.instant.post(joinQueue)
            ActivityLogController.instant.logQueue(
                joinQueue.queue_code,
                CREATE
            ) { "start:${joinQueue.start_time} end:${joinQueue.end_time}" }
            call.respond(HttpStatusCode.Created, create)
        }
        get("/join/queue") {
            require(call.request.header("api-key") == config.apiKey) { "API KEY Not cCC" }
            val queueCode = call.request.queryParameters["queue_code"]
            require(!queueCode.isNullOrBlank()) { "ข้อมูล queue_code มีค่าว่าง" }
            call.respond(JoinController.instant.get(queueCode))
        }

        get("/join/queue/{queue_code}") {
            require(call.request.header("api-key") == config.apiKey) { "API KEY Not cCC" }
            val queueCode = call.parameters["queue_code"]
            require(!queueCode.isNullOrBlank()) { "ข้อมูล queue_code มีค่าว่าง" }
            call.respond(JoinController.instant.get(queueCode))
        }
        get("/join/queue_input_test") {
            call.respond(JoinController.instant.getTest())
        }
        get("/join/queue_response_test") {
            call.respond(JoinController.instant.getResponseTest())
        }
        /**
         * Vdo
         */
        get("/vdo/session/{session_name}/webrtctoken") {
            getLogger(this::class.java).info("Call webrtctoken")
            val queue_code = call.parameters["session_name"]!!
            val token = VdoServerController.instant.createUserWebRTCToken(queue_code)
            ActivityLogController.instant.logQueue(queue_code, TOKEN) { "" }
            call.respond(token)
        }

    }
}