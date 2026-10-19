package mni.siddique.battery_state_android.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import mni.siddique.battery_state_android.usecase.BatteryStateObserver
import mni.siddique.battery_state_android.usecase.SystemEvent

class BatteryStateReceiver(
    private val batteryStateObserver: BatteryStateObserver
) : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        val systemEvent = SystemEvent.fromIntentAction(action) ?: return
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        batteryStateObserver.observe(level, scale, systemEvent)
    }
}