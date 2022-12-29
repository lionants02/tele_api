package th.nstda.thongkum.tele_api.activity_log

import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Ignore
import org.junit.Test

@Ignore

class ActivityLogControllerTest {
    @Test
    fun testEnum() {
        val t = TEST.T2
        println(t.name)
        println(t.toString())
    }

    @Test
    fun activityTest() {
        val dao = ActivityLogController.instant

        runBlocking {
            dao.log(ActivityLog.TYPE.INFO) { "Test Info" }
            delay(1000)
            dao.log(ActivityLog.TYPE.ERROR) { "Test Error" }
            delay(1000)
            dao.log(ActivityLog.TYPE.DEBUG) { "Test Debug" }
            delay(1000)
            dao.log(ActivityLog.TYPE.WARN) { "Test Warn" }
            delay(1000)
            dao.logQueue("test001", ActivityLog.ACTIVITY.CREATE) { "Create" }
            dao.logQueue("test001", ActivityLog.ACTIVITY.UPDATE) { "Update" }
            delay(1000)
            dao.logQueue("test002", ActivityLog.ACTIVITY.CREATE) { "Create" }
            dao.logQueue("test001", ActivityLog.ACTIVITY.DELETE) { "Delete" }
        }

    }

    enum class TEST {
        T2
    }
}