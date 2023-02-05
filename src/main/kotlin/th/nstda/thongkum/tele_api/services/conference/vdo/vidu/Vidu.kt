package th.nstda.thongkum.tele_api.services.conference.vdo.vidu

interface Vidu {
    fun haveSession(sessionName: String): Boolean
    fun getConnection(sessionName: String, name: String): String
    fun getSessions(): List<String>
    fun closeSession(sessionName: String)
}
