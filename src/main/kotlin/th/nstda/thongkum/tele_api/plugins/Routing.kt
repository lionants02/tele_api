package th.nstda.thongkum.tele_api.plugins

import io.ktor.server.routing.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable
import th.nstda.thongkum.tele_api.getLogger

fun Application.configureRouting() {
    routing {
        get("/") {
            getLogger(this::class.java).info("test Thai ทดสอบไทย")
            val response = Detail(
                "Apache License 2.0",
                "http://www.apache.org/licenses/",
                "https://lionants02.github.io/tele_api/",
                arrayOf(Contributor("Thanachai Thongkum", "https://github.com/lionants02"))
            )
            call.respond(response)
        }
        get("/test") {
            call.respond("Hello World!")
        }

    }
}

@Serializable
internal data class Contributor(val name: String, val url: String)

@Serializable
internal data class Detail(
    val license: String,
    val licenseUrl: String,
    val docUrl: String,
    val developers: Array<Contributor>
)
