package th.nstda.thongkum.tele_api.services.conference.vdo

import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table

object VdoServerExpose : Table() {
    val api = varchar("api", 100)
    val secret = varchar("secret", 100)
    val weight = integer("weight")
    fun mapResult(result: ResultRow): VdoServerData {
        return VdoServerData(
            result[api],
            result[secret],
            result[weight]
        )
    }
}