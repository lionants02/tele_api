package th.nstda.thongkum.tele_api.services.conference.vdo.vidu

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import th.nstda.thongkum.tele_api.getLogger

class ViduRest(private val viduSecret: ViduSecret) : Vidu {
    override fun haveSession(sessionName: String): Boolean {
        val status = runBlocking {
            val response = httpClient.get("${viduSecret.apiLink}/sessions/$sessionName") {
                basicAuth("OPENVIDUAPP", viduSecret.secretVdo)
            }
            response.status
        }
        return status.isSuccess()
    }

    override fun closeSession(sessionName: String) {
        runBlocking {
            val response = httpClient.delete("${viduSecret.apiLink}/sessions/$sessionName") {
                basicAuth("OPENVIDUAPP", viduSecret.secretVdo)
            }
            log.info("Close vidu session $sessionName is response ${response.status.description}:${response.status.value}")
        }
    }

    override fun getConnection(sessionName: String, name: String): String {
        return runBlocking {

            val kurentoOptions = KurentoOptions(250, 100, 250, 100)
            // val data ="{\"clientData\":\"Participant73\"}"
            val requestConnection = RequestConnection("WEBRTC", null, false, "PUBLISHER", kurentoOptions)
            val response = httpClient.post("${viduSecret.apiLink}/sessions/$sessionName/connection") {
                basicAuth("OPENVIDUAPP", viduSecret.secretVdo)
                setBody(requestConnection)
                contentType(ContentType.Application.Json)
            }
            require(response.status.isSuccess()) { "Cannot success ${response.status}" }
            val responseData: ReturnConnection = response.body()
            responseData.token!!
        }
    }

    override fun getSessions(): List<String> {
        return runBlocking {
            val response = httpClient.get("${viduSecret.apiLink}/sessions") {
                basicAuth("OPENVIDUAPP", viduSecret.secretVdo)
                accept(ContentType.Application.Json)
            }
            require(response.status.isSuccess()) { "Cannot success ${response.status}" }
            val responseData: SessionProperty = response.body()
            responseData.content
        }.filter { it.`object` == "session" }.map { it.sessionId }
    }

    @Serializable
    internal data class SessionProperty(
        val numberOfElements: Int,
        val content: Array<Session>
    )

    @Serializable
    internal data class Session(
        val id: String,
        val sessionId: String,
        val `object`: String,
        val createdAt: Long
    )

    @Serializable
    internal data class RequestConnection(
        val type: String,
        val data: String?,
        val record: Boolean,
        val role: String,
        val kurentoOptions: KurentoOptions
    )

    @Serializable
    internal data class KurentoOptions(
        val videoMaxRecvBandwidth: Int,
        val videoMinRecvBandwidth: Int,
        val videoMaxSendBandwidth: Int,
        val videoMinSendBandwidth: Int
    )

    @Serializable
    internal data class ReturnConnection(
        val type: String?,
        val record: Boolean?,
        val role: String?,
        val kurentoOptions: KurentoOptions?,
        val sessionId: String?,
        val createdAt: Long?,
        val activeAt: Long?,
        val location: String?,
        val ip: String?,
        val platform: String?,
        val token: String?,
        val serverData: String?,
        val clientData: String?
    )

    companion object {
        private val httpClient by lazy {
            HttpClient(CIO) {
                expectSuccess = false
                install(Logging) {
                    logger = Logger.DEFAULT
                    level = LogLevel.HEADERS
                }
                install(ContentNegotiation) {
                    json(Json {
                        ignoreUnknownKeys = true
                    })
                }
            }
        }
        private val log by lazy { getLogger(ViduRest::class.java) }
    }
}