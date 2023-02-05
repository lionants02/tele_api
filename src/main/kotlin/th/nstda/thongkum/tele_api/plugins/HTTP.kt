package th.nstda.thongkum.tele_api.plugins

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.plugins.cors.routing.*
import io.ktor.server.plugins.forwardedheaders.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import kotlinx.serialization.Serializable

fun Application.configureHTTP() {
    install(CachingHeaders) {
        options { call, outgoingContent ->
            when (outgoingContent.contentType?.withoutParameters()) {
                ContentType.Text.CSS -> CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 60))
                else -> null
            }
        }
    }
    install(ForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
    install(XForwardedHeaders) // WARNING: for security, do not include this if not behind a reverse proxy
    install(CORS) {
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Delete)
        allowMethod(HttpMethod.Patch)
        allowHeader(HttpHeaders.Authorization)
        allowHeader("MyCustomHeader")
        allowHeader(HttpHeaders.AccessControlAllowHeaders)
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.AccessControlAllowOrigin)
        allowHeader(HttpHeaders.Cookie)
        allowCredentials = true
        maxAgeInSeconds = 1
        anyHost()
    }
    install(StatusPages) {
        exception { call: ApplicationCall, cause: Exception ->
            when (cause) {
                is MissingRequestParameterException -> call.respond(
                    HttpStatusCode.BadRequest,
                    ExceptionResponse(cause.message ?: "", HttpStatusCode.BadRequest.value)
                )

                is BadRequestException -> call.respond(
                    HttpStatusCode.BadRequest,
                    ExceptionResponse(cause.message ?: "", HttpStatusCode.BadRequest.value)
                )

                is IllegalArgumentException -> call.respond(
                    HttpStatusCode.BadRequest,
                    ExceptionResponse("IllegalArgumentException ${cause.message}", HttpStatusCode.BadRequest.value)
                )

                is NoSuchElementException -> call.respond(
                    HttpStatusCode.NotFound,
                    ExceptionResponse(cause.message ?: "", HttpStatusCode.NotFound.value)
                )

                is NullPointerException -> call.respond(
                    HttpStatusCode.NotFound,
                    ExceptionResponse(cause.message ?: "", HttpStatusCode.NotFound.value)
                )

                else -> call.respond(
                    HttpStatusCode.InternalServerError,
                    ExceptionResponse("Error 500", HttpStatusCode.InternalServerError.value)
                )
            }
        }
    }
    // openAPI(path="openapi")

}

@Serializable
data class ExceptionResponse(
    val message: String,
    val code: Int
)