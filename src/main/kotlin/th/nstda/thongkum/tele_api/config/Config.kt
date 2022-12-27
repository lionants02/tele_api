package th.nstda.thongkum.tele_api.config

interface Config {
    val openviduDefaultUrl: String
    val openviduDefaultSecret: String
    val hikariConfigFile: String
    val frontEnd: String
}