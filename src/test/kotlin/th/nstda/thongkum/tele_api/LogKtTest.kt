package th.nstda.thongkum.tele_api

import kotlinx.datetime.*
import org.junit.Test


class LogKtTest {

    @Test
    fun getLogger() {
        val log = getLogger(TestLoggerClass::class.java)
        log.info("Test log")
    }

    @Test
    fun convertTimt() {
        val testDate = "2023-01-04"
        val testTime = "19:30:00"
        val concat = "${testDate}T${testTime}.000Z"
        val instant = concat.toInstant()
        assert(instant.toString() == "2023-01-04T19:30:00Z")
    }

    @Test
    fun revertDateTime() {
        val testDate = "2023-01-04"
        val testTime = "19:30:00"
        val concat = "${testDate}T${testTime}.000Z"
        val localDateTime = concat.toInstant()
        val strOutput = localDateTime.toString()
        println(strOutput)
        val resultDate = strOutput.substring(0, 10)
        val resultTime = strOutput.substring(11, 19)
        assert(resultDate == testDate) { "$testDate != $resultDate" }
        assert(resultTime == testTime) { "$testTime != $resultTime" }
    }

    internal class TestLoggerClass
}