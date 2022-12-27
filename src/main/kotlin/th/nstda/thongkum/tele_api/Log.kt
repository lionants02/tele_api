package th.nstda.thongkum.tele_api

import org.slf4j.Logger
import org.slf4j.LoggerFactory

fun <T>getLogger(clazz: Class<T>): Logger {
    return LoggerFactory.getLogger(clazz)
}