package th.nstda.thongkum.tele_api.services.conference.vdo

import org.junit.Test

class UtilTest {
    @Test
    fun rndServerWithWeightTest1() {
        val servers = listOf<VdoServerData>(
            VdoServerData("test1", "test1", 1),
            VdoServerData("test2", "test2", 1),
            VdoServerData("test3", "test3", 5)
        )
        println(Util.rndServerWithWeight(servers).api)
        println(Util.rndServerWithWeight(servers).api)
        println(Util.rndServerWithWeight(servers).api)
        println(Util.rndServerWithWeight(servers).api)
        println(Util.rndServerWithWeight(servers).api)
        println(Util.rndServerWithWeight(servers).api)
    }

    @Test
    fun rndServerWithWeightTest2() {
        val servers = listOf<VdoServerData>(
            VdoServerData("test1", "test1", 1)
        )
        println(Util.rndServerWithWeight(servers).api)
        println(Util.rndServerWithWeight(servers).api)
        println(Util.rndServerWithWeight(servers).api)
        println(Util.rndServerWithWeight(servers).api)
        println(Util.rndServerWithWeight(servers).api)
        println(Util.rndServerWithWeight(servers).api)
    }
}