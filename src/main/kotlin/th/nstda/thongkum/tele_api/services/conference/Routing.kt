package th.nstda.thongkum.tele_api.services.conference

import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
    }
}
