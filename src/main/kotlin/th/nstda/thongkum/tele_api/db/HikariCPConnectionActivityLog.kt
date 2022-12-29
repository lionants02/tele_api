package th.nstda.thongkum.tele_api.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import th.nstda.thongkum.tele_api.config
import th.nstda.thongkum.tele_api.getLogger
import java.io.File
import javax.sql.DataSource

/**
 * ใช้สำหรับเชื่อมต่อ database
 */
abstract class HikariCPConnectionActivityLog {
    init {
        try {
            Database.connect(ds)
        } catch (ex: Exception) {
            getLogger(HikariCPConnectionActivityLog::class.java).error(ex.message, ex)
            throw ex
        }
    }

    companion object {
        private val ds: DataSource by lazy {
            val log = getLogger(HikariCPConnectionActivityLog::class.java)
            try {
                val configFile = config.hikariActivityLog
                val file = File(configFile)
                val isFile = file.isFile
                log.info("Hikari config is file $isFile")
                require(isFile) { "${file.absolutePath} ไม่ใช่ไฟล์" }
                val hkConfig = HikariConfig(configFile)
                log.info("HikariCP Config = $hkConfig")
                HikariDataSource(hkConfig)
            } catch (ex: Exception) {
                log.error(ex.message, ex)
                throw ex
            }
        }
    }
}