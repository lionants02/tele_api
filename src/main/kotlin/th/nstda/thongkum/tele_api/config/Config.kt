package th.nstda.thongkum.tele_api.config

/**
 * สำหรับอ้างอิง ในระบบดึงค่าตัวแปรต่างๆ จากระบบ
 */
interface Config {
    val openviduDefaultUrl: String
    val openviduDefaultSecret: String
    val hikariConfigFile: String
    val frontEnd: String
    val apiKey: String
    val enterEarlySec: Int
}