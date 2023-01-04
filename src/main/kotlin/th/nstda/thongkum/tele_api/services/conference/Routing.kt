package th.nstda.thongkum.tele_api.services.conference

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.server.application.*
import io.ktor.server.plugins.cachingheaders.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import th.nstda.thongkum.tele_api.activity_log.ActivityLog.ACTIVITY.*
import th.nstda.thongkum.tele_api.activity_log.ActivityLog.ActivityHttpHeader
import th.nstda.thongkum.tele_api.activity_log.ActivityLogController
import th.nstda.thongkum.tele_api.config
import th.nstda.thongkum.tele_api.getLogger
import th.nstda.thongkum.tele_api.services.conference.join.JoinController
import th.nstda.thongkum.tele_api.services.conference.join.JoinQueue2Data
import th.nstda.thongkum.tele_api.services.conference.join.JoinQueueData
import th.nstda.thongkum.tele_api.services.conference.vdo.VdoServerController
import th.nstda.thongkum.tele_api.services.cron.CronTaskController

fun Application.configureVdoRouting() {

    routing {
        /**
         * Join
         */
        post("/join/queue") {
            require(call.request.header("api-key") == config.apiKey) { "API KEY Not cCC" }
            val joinQueue = call.receive<JoinQueueData>()
            val create = JoinController.instant.post(joinQueue)
            runBlocking {
                launch {
                    ActivityLogController.instant.logQueue(
                        joinQueue.queue_code, CREATE, getHeaderLog(call)
                    ) { "start:${joinQueue.start_time} end:${joinQueue.end_time}" }
                }
                launch {
                    call.caching = CachingOptions(CacheControl.NoCache(null))
                    call.respond(HttpStatusCode.Created, create)
                }
            }
        }
        post("/join/queuev2") {
            require(call.request.header("api-external-access-code") == config.apiKey) { "API KEY Not cCC" }
            val joinQueue = call.receive<JoinQueue2Data>()
            val create = JoinController.instant.postV2(joinQueue)
            runBlocking {
                launch {
                    ActivityLogController.instant.logQueue(
                        joinQueue.queue_code, CREATE, getHeaderLog(call)
                    ) { "start:${joinQueue.reserve_date}T${joinQueue.reserve_time}.000 duration:${joinQueue.duration}" }
                }
                launch {
                    call.caching = CachingOptions(CacheControl.NoCache(null))
                    call.respond(HttpStatusCode.Created, create)
                }
            }
        }
        put("/join/queue/{queue_code}") {
            require(call.request.header("api-key") == config.apiKey) { "API KEY Not cCC" }
            val queueCode = call.parameters["queue_code"]
            require(!queueCode.isNullOrBlank()) { "ข้อมูล queue_code มีค่าว่าง" }
            val joinQueue = call.receive<JoinQueueData>()
            val update = JoinController.instant.update(queueCode, joinQueue)
            runBlocking {
                launch {
                    ActivityLogController.instant.logQueue(
                        joinQueue.queue_code, UPDATE, getHeaderLog(call)
                    ) { "start:${joinQueue.start_time} end:${joinQueue.end_time}" }
                }
                launch {
                    call.caching = CachingOptions(CacheControl.NoCache(null))
                    call.respond(HttpStatusCode.Created, update)
                }
            }

        }
        get("/join/queue") {
            require(call.request.header("api-key") == config.apiKey) { "API KEY Not cCC" }
            val queueCode = call.request.queryParameters["queue_code"]
            require(!queueCode.isNullOrBlank()) { "ข้อมูล queue_code มีค่าว่าง" }
            call.caching = CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 300))
            call.respond(JoinController.instant.get(queueCode))
        }

        get("/cron") {
            require(call.request.header("api-key") == config.apiKey) { "API KEY Not cCC" }
            CronTaskController().run()
            call.caching = CachingOptions(CacheControl.NoCache(null))
            call.respond(HttpStatusCode.OK, mapOf("status" to "ok"))
        }

        get("/join/queue/{queue_code}") {
            require(call.request.header("api-key") == config.apiKey) { "API KEY Not cCC" }
            val queueCode = call.parameters["queue_code"]
            require(!queueCode.isNullOrBlank()) { "ข้อมูล queue_code มีค่าว่าง" }
            call.caching = CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 300))
            call.respond(JoinController.instant.get(queueCode))
        }
        get("/join/queue_input_test") {
            call.caching = CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 3000))
            call.respond(JoinController.instant.getTest())
        }
        get("/join/queue_response_test") {
            call.caching = CachingOptions(CacheControl.MaxAge(maxAgeSeconds = 3000))
            call.respond(JoinController.instant.getResponseTest())
        }
        /**
         * Vdo
         */
        get("/vdo/session/{session_name}/webrtctoken") {
            getLogger(this::class.java).info("Call webrtctoken")
            val header = getHeaderLog(call)
            require(!header.user_agent.contains("Line")) { "เปิดผ่าน Line ไม่ได้ Can't open line browser." }
            require(!header.user_agent.contains("FB_IAB")) { "เปิดผ่าน Facebook message ไม่ได้ Can't open Facebook message browser." }
            require(!header.user_agent.contains("FBAV")) { "เปิดผ่าน Facebook message ไม่ได้ Can't open Facebook message browser." }
            val queue_code = call.parameters["session_name"]!!
            val token = VdoServerController.instant.createUserWebRTCToken(queue_code)


            runBlocking {
                launch {
                    ActivityLogController.instant.logQueue(queue_code, TOKEN, header) { "" }
                }
                launch {
                    call.caching = CachingOptions(CacheControl.NoCache(null))
                    call.respond(token)
                }
            }
        }

    }
}

private fun getHeaderLog(call: ApplicationCall): ActivityHttpHeader {
    val req = call.request
    val xForwardFor = req.header("X-Forwarded-For") ?: req.header("x-forwarded-for") ?: ""
    val cfIpCountry = req.header("CF-IPCountry") ?: req.header("cf-ipcountry") ?: ""
    val userAgent = req.header("User-Agent") ?: req.header("user-agent") ?: "" // User-Agent
    val referer = req.header("Referer") ?: req.header("referer") ?: ""// Referer
    return ActivityHttpHeader(xForwardFor, cfIpCountry, userAgent, referer)
}