package mni.siddique.battery_state_android.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager

class BatteryStateReceiver(private val observer: BatteryStateObserver) : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        observer.isBatteryStateNotifiable(
            intent?.action,
            intent?.getIntExtra(BatteryManager.EXTRA_LEVEL, -1),
            intent?.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        )
    }
}