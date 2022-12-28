package th.nstda.thongkum.tele_api.config

import org.kohsuke.args4j.CmdLineException
import org.kohsuke.args4j.CmdLineParser
import org.kohsuke.args4j.Option
import th.nstda.thongkum.tele_api.getLogger

class ConfigWithParameter(args: Array<String>) : Config {

    @Option(name = "-vidu", usage = "vidu http api")
    private var _openviduDefaultUrl = ""

    @Option(name = "-vidu_defult_secret", usage = "vidu secret api")
    private var _openviduDefaultSecret = ""

    @Option(name = "-hikarifile", usage = "hikari config file")
    private var _hikariConfigFile = ""

    @Option(name = "-front_end", usage = "front end link https://example.hii.in.th")
    private var _frontEnd = ""

    @Option(name = "-api_key", usage = "api_key")
    private var _apiKey = ""

    override val openviduDefaultUrl: String
        get() = _openviduDefaultUrl.ifBlank { System.getenv("VIDU_DEFAULT_URL") }
            .ifBlank { throw Exception("VIDU_DEFAULT_URL Bank") }
    override val openviduDefaultSecret: String
        get() = _openviduDefaultSecret.ifBlank { System.getenv("VIDU_SECRET") }
            .ifBlank { throw Exception("VIDU_SECRET Bank") }

    override val hikariConfigFile: String
        get() = _hikariConfigFile.ifBlank { System.getenv("HIKARI_CONFIG_FILE") }
            .ifBlank { throw Exception("HIKARI_CONFIG_FILE Bank") }

    override val frontEnd: String
        get() = _frontEnd.ifBlank { System.getenv("TELE_FRONT_END") }.ifBlank { "https://example.hii.in.th" }

    override val apiKey: String
        get() = _apiKey.ifBlank { System.getenv("API_KEY") }.ifBlank { "bBIFF5zkWq2oleJTVV1OviKCvSFkSCrguHGB" }

    init {
        try {
            getLogger(ConfigWithParameter::class.java).info("Java version ${System.getProperty("java.version")}")
            println(args.toList())
            CmdLineParser(this).parseArgument(*args)
        } catch (ex: CmdLineException) {
            throw ex
        }
    }
}