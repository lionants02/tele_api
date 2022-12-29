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

    @Option(name = "-hikariactivityfile", usage = "hikari activity config file")
    private var _hikariActivityFile = ""

    @Option(name = "-front_end", usage = "front end link https://example.hii.in.th")
    private var _frontEnd = ""

    @Option(name = "-api_key", usage = "api_key")
    private var _apiKey = ""


    override val openviduDefaultUrl: String
        get() = _openviduDefaultUrl.ifBlank { "VIDU_DEFAULT_URL".systemEnv }
            .ifBlank { throw Exception("VIDU_DEFAULT_URL Bank") }
    override val openviduDefaultSecret: String
        get() = _openviduDefaultSecret.ifBlank { "VIDU_SECRET".systemEnv }
            .ifBlank { throw Exception("VIDU_SECRET Bank") }

    override val hikariConfigFile: String
        get() = _hikariConfigFile.ifBlank { "HIKARI_CONFIG_FILE".systemEnv }
            .ifBlank { throw Exception("HIKARI_CONFIG_FILE Bank") }

    override val frontEnd: String
        get() = _frontEnd.ifBlank { "TELE_FRONT_END".systemEnv }.ifBlank { "https://example.hii.in.th" }

    override val apiKey: String
        get() = _apiKey.ifBlank { "API_KEY".systemEnv }.ifBlank { "bBIFF5zkWq2oleJTVV1OviKCvSFkSCrguHGB" }

    override val hikariActivityLog: String
        get() = _hikariActivityFile.ifBlank { "HIKARI_CONFIG_ACTIFITY_FILE".systemEnv }.ifBlank { "" }

    private val String.systemEnv: String
        get() = System.getenv(this) ?: ""

    init {
        try {
            getLogger(ConfigWithParameter::class.java).info("Run with java version ${System.getProperty("java.version")}")
            println(args.toList())
            CmdLineParser(this).parseArgument(*args)
        } catch (ex: CmdLineException) {
            throw ex
        }
    }
}