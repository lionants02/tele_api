package th.nstda.thongkum.tele_api.services.conference.vdo

import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import th.nstda.thongkum.tele_api.services.conference.db.HikariCPConnection

class VdoServerController : HikariCPConnection() {
    fun getServer(): VdoServerData {
        val servers = transaction {
            SchemaUtils.create(VdoServerExpose)
            VdoServerExpose.selectAll().map {
                VdoServerExpose.mapResult(it)
            }.toList()
        }.toList()

        val count = servers.size
        return servers.first()
    }

    companion object {
        val instant by lazy { VdoServerController() }
    }
}