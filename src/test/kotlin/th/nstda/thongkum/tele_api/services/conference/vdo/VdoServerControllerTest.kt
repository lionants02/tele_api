package th.nstda.thongkum.tele_api.services.conference.vdo

import org.junit.Ignore
import org.junit.Test

@Ignore
class VdoServerControllerTest {

    @Test
    fun getServer() {
        val server = VdoServerController.instant.getServer()
        println(server)
    }
}