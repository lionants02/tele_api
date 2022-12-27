package th.nstda.thongkum.tele_api

import org.junit.Test


class LogKtTest {

    @Test
    fun getLogger() {
        val log = getLogger(TestLoggerClass::class.java)
        log.info("Test log")
    }

    internal class TestLoggerClass
}