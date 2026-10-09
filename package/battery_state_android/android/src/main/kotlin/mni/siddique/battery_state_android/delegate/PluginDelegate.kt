package mni.siddique.battery_state_android.delegate

interface PluginDelegate {
    suspend fun call(name: String, argument: String): PluginReply
}