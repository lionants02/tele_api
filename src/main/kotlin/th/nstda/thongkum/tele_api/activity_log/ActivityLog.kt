package th.nstda.thongkum.tele_api.activity_log

interface ActivityLog {
    fun log(type: TYPE, message: () -> String)
    fun logQueue(queueCode: String, activity: ACTIVITY, message: () -> String)

    enum class TYPE {
        INFO, DEBUG, WARN, ERROR
    }

    enum class ACTIVITY {
        CREATE, UPDATE, DELETE
    }

}