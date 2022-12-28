package th.nstda.thongkum.tele_api

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import th.nstda.thongkum.tele_api.config.Config
import th.nstda.thongkum.tele_api.config.ConfigWithParameter
import th.nstda.thongkum.tele_api.plugins.configureHTTP
import th.nstda.thongkum.tele_api.plugins.configureRouting
import th.nstda.thongkum.tele_api.plugins.configureSerialization
import th.nstda.thongkum.tele_api.services.conference.configureVdoRouting

private var privateConfig: Config? = null
val config: Config
    get() =
        if (privateConfig != null)
            privateConfig!!
        else
            ConfigWithParameter(arrayOf())

fun main(args: Array<String>) {
    privateConfig = ConfigWithParameter(args)
    getLogger(Unit::class.java).info("vidu url ${config.openviduDefaultUrl} ${config.openviduDefaultSecret}")
    embeddedServer(Netty, port = 8080, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun injectConfig(config: Config) {
    privateConfig = config
}

fun Application.module() {
    configureHTTP()
    configureSerialization()
    configureRouting()
    configureVdoRouting()
}
