package mni.siddique.battery_state_android.usecase

import android.content.Intent

enum class SystemEvent {
    BATTERY_CHANGED,
    POWER_DISCONNECTED;

    companion object {
        fun fromIntentAction(action: String): SystemEvent? {
            return when (action) {
                Intent.ACTION_BATTERY_CHANGED -> BATTERY_CHANGED
                Intent.ACTION_POWER_DISCONNECTED -> POWER_DISCONNECTED
                else -> null
            }
        }
    }
}