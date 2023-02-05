package th.nstda.thongkum.tele_api.services.conference.vdo.vidu

data class ViduSecret(val apiVdo: String, val secretVdo: String) {
    val apiLink get() = "$apiVdo/openvidu/api"
}