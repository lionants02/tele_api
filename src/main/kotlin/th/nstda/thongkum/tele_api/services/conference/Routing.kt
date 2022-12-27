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
         * ดึงรายการห้องใน org นี้
         */
        get("/vdo/{org_id}/room") {
            val orgId = call.parameters["org_id"]
            call.respondText("List room at org $orgId")
        }
        /**
         * สร้างห้องภายใน org นี้
         */
        post("/vdo/{org_id}/room") {
            val orgId = call.parameters["org_id"]
            call.respondText("create room at org $orgId")
        }
        /**
         * ลบห้องภายใน org นี้
         */
        delete("/vdo/{org_id}/room/{room_id}") {
            val orgId = call.parameters["org_id"]
            val roomId = call.parameters["room_id"]
            call.respondText("Delete room org $orgId room $roomId")
        }

        /**
         * ขอ token สำหรับเข้าห้อง
         */
        get("/vdo/{org_id}/room/{room_id}/join") {
            val orgId = call.parameters["org_id"]
            val roomId = call.parameters["room_id"]
            call.respondText("Join room org $orgId room $roomId")
        }
        /**
         * สร้างห้องภายใน org นี้
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