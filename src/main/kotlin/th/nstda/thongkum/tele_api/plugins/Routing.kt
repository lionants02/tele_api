package th.nstda.thongkum.tele_api.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import th.nstda.thongkum.tele_api.getLogger

fun Application.configureRouting() {

    routing {
        get("/") {
            getLogger(this::class.java).info("ทดสอบไทย")
            call.respondText("Hello World!")
        }
    }
}
