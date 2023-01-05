package th.nstda.thongkum.tele_api.services.conference.join

import kotlinx.datetime.TimeZone
import kotlinx.datetime.toInstant
import kotlinx.datetime.toLocalDateTime
import org.junit.After
import org.junit.Before

import org.junit.Test
import th.nstda.thongkum.tele_api.services.conference.join.Util.Companion.convertDataToData2
import kotlin.time.DurationUnit
import kotlin.time.toDuration

class JoinControllerTest {

    @Before
    fun setUp() {
    }

    @After
    fun tearDown() {
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

    @Test
    fun convertDataToData2() {
        val join = JoinQueueData(
            "test",
            "2022-12-27T15:08:51.716579500".toLocalDateTime(),
            "2023-01-10T22:08:51.718584300".toLocalDateTime()
        )
        val convert = convertDataToData2(join)
        assert(convert.duration == 20580) { "Test ${convert.duration} != 20580" }
    }

    @Test
    fun testConvert30Min() {
        val clockTest = "2022-12-27T15:08:51.716579500Z".toInstant()
        val join = JoinQueueData(
            "test",
            clockTest.toLocalDateTime(TimeZone.UTC),
            clockTest.plus(30.toDuration(DurationUnit.MINUTES)).toLocalDateTime(TimeZone.UTC)
        )
        val convert = convertDataToData2(join)
        assert(convert.duration == 30) { "Test ${convert.duration} != 30" }
    }

    @Test
    fun testConvert1Day() {
        val clockTest = "2022-12-27T15:08:51.716579500Z".toInstant()
        val join = JoinQueueData(
            "test",
            clockTest.toLocalDateTime(TimeZone.UTC),
            clockTest.plus(1.toDuration(DurationUnit.DAYS)).toLocalDateTime(TimeZone.UTC)
        )
        val convert = convertDataToData2(join)
        assert(convert.duration == 1440) { "Test ${convert.duration} != 1440" }
    }
}