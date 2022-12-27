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


    override val openviduDefaultUrl: String
        get() = _openviduDefaultUrl.ifBlank { System.getenv("VIDU_DEFAULT_URL") }
    override val openviduDefaultSecret: String
        get() = _openviduDefaultSecret.ifBlank { System.getenv("VIDU_SECRET") }

    override val hikariConfigFile: String
        get() = _hikariConfigFile.ifBlank { System.getenv("HIKARI_CONFIG_FILE") }

    override val frontEnd: String
        get() = "https://example.hii.in.th"

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