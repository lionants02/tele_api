package th.nstda.thongkum.tele_api.services.conference.vdo

import org.junit.Before
import org.junit.Ignore
import org.junit.Test
import th.nstda.thongkum.tele_api.config.ConfigWithParameter
import th.nstda.thongkum.tele_api.injectConfig

@Ignore
class VdoServerControllerTest {

    private val dao = VdoServerController.instant

    @Before
    fun setUp() {
        val config = ConfigWithParameter(arrayOf())
        injectConfig(config)
    }

    @Test
    fun getServer() {
        val server = dao.getServer()
        println(server)
    }

    @Test
    fun creteSession() {
        dao.creteSession("sddfe453q")
    }

    fun createUserToken() {
        val token = dao.createUserWebRTCToken("sddfe453q")
        println("Token $token")
    }
}