package mni.siddique.battery_state_android.delegate

sealed class PluginReply {
    data class Success(val value: String) : PluginReply()
    data class Error(val code: String, val message: String) : PluginReply()
    object NotImplemented : PluginReply()
}