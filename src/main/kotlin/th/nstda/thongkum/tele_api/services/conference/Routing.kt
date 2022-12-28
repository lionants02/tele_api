package th.nstda.thongkum.tele_api.services.conference

import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import th.nstda.thongkum.tele_api.services.conference.join.JoinController
import th.nstda.thongkum.tele_api.services.conference.join.JoinQueueData

fun Application.configureVdoRouting() {

    routing {
        /**
         * Join
         */
        post("/join/queue") {
            val joinQueue = call.receive<JoinQueueData>()
            call.respond(JoinController.instant.post(joinQueue))
        }
        get("/join/queue") {
            val queueCode = call.request.queryParameters["queue_code"]
            require(!queueCode.isNullOrBlank()){"ข้อมูล queue_code มีค่าว่าง"}
            call.respond(JoinController.instant.get(queueCode))
        }

        get("/join/queue/{queue_code}") {
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
    }
}