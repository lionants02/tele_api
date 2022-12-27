package th.nstda.thongkum.tele_api.services.conference.db

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import th.nstda.thongkum.tele_api.config
import th.nstda.thongkum.tele_api.getLogger
import java.io.File
import javax.sql.DataSource


abstract class HikariCPConnection {
    init {
        try {
            Database.connect(ds)
        } catch (ex: Exception) {
            getLogger(HikariCPConnection::class.java).error(ex.message, ex)
            throw ex
        }
    }

    companion object {
        private val ds: DataSource by lazy {
            val log = getLogger(HikariCPConnection::class.java)
            try {
                val file = File(config.hikariConfigFile)
                val isFile = file.isFile
                log.info("Hikari config is file $isFile")
                require(isFile) { "${file.absolutePath} ไม่ใช่ไฟล์" }
                val hkConfig = HikariConfig(config.hikariConfigFile)
                log.info("HikariCP Config = $hkConfig")
                HikariDataSource(hkConfig)
            } catch (ex: Exception) {
                log.error(ex.message, ex)
                throw ex
            }
        }
    }
}