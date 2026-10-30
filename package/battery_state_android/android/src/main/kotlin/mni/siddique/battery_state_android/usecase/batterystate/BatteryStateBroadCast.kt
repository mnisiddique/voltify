package mni.siddique.battery_state_android.usecase.batterystate

import android.content.Intent
import android.os.BatteryManager

data class BatteryStateBroadCast(
    val batteryLevel: Int,
    val batteryScale: Int,
    val event: SystemEvent
) {
    companion object {
        fun fromIntent(intent: Intent): BatteryStateBroadCast {
            val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
            val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
            val event = SystemEvent.fromIntentAction(intent.action)
            return BatteryStateBroadCast(level, scale, event)
        }
    }
}