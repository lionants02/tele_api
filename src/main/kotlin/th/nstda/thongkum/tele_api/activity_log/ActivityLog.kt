package th.nstda.thongkum.tele_api.activity_log

interface ActivityLog {
    fun log(type: TYPE, message: () -> String)
    fun logQueue(queueCode: String, activity: ACTIVITY, message: () -> String)
    fun logQueue(
        queueCode: String,
        activity: ACTIVITY,
        activityHttpHeader: ActivityHttpHeader? = null, message: () -> String
    )

    enum class TYPE {
        INFO, DEBUG, WARN, ERROR
    }

    enum class ACTIVITY {
        CREATE, UPDATE, DELETE, TOKEN
    }

    data class ActivityHttpHeader(
        var x_forwarded_for: String,
        var cf_ipcountry: String,
        var user_agent: String, // User-Agent
        var referer: String // Referer
    )

}