package th.nstda.thongkum.tele_api.services.conference.vdo.vidu

import org.junit.Before
import org.junit.Test

import org.junit.Assert.*
import org.junit.Ignore
import th.nstda.thongkum.tele_api.config
import th.nstda.thongkum.tele_api.config.ConfigWithParameter
import th.nstda.thongkum.tele_api.injectConfig

@Ignore
class ViduRestTest {
    private val dao by lazy { ViduRest(ViduSecret(config.openviduDefaultUrl, config.openviduDefaultSecret)) }

    @Before
    fun setUp() {
        val config = ConfigWithParameter(arrayOf())
        injectConfig(config)
    }

    @Test
    fun haveSession() {
        val check = dao.haveSession("12345101")
        assertEquals(true, check)
    }

    @Test
    fun getSession() {
        val sessions = dao.getSessions()
        println(sessions.first())
    }
}