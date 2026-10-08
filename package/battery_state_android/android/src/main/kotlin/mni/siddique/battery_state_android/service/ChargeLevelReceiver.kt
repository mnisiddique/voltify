package mni.siddique.battery_state_android.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import mni.siddique.battery_state_android.usecase.ChargeLevelObserver
import mni.siddique.battery_state_android.usecase.SystemEvent

class ChargeLevelReceiver(
    private val chargeLevelObserver: ChargeLevelObserver
) : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        val action = intent?.action ?: return
        val systemEvent = SystemEvent.fromIntentAction(action) ?: return
        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
        chargeLevelObserver.observe(level, scale, systemEvent)
    }
}