package th.nstda.thongkum.tele_api.services.conference.vdo

import kotlin.random.Random

internal class Util {

    companion object {
        fun rndServerWithWeight(servers: List<VdoServerData>): VdoServerData {
            val totalWeight = servers.filter { it.weight > 0 }.sumOf { it.weight }
            var randomIndex = -1
            var random: Double = Random.nextDouble() * totalWeight
            for (i in servers.indices) {
                random -= servers[i].weight
                if (random <= 0.0) {
                    randomIndex = i
                    break
                }
            }
            return servers[randomIndex]
        }
    }
}