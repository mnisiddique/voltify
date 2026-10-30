package mni.siddique.battery_state_android.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.BatteryManager
import mni.siddique.battery_state_android.usecase.batterystate.AnalyzeBatteryStateUseCase
import mni.siddique.battery_state_android.usecase.batterystate.BatteryStateBroadCast
import mni.siddique.battery_state_android.usecase.batterystate.BatteryStateObserver
import mni.siddique.battery_state_android.usecase.batterystate.SystemEvent

class BatteryStateReceiver(
    private val analyzeBatteryStateUseCase: AnalyzeBatteryStateUseCase
) : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        if (intent == null) return
        val batteryStateBroadCast = BatteryStateBroadCast.fromIntent(intent)
        analyzeBatteryStateUseCase.analyze(batteryStateBroadCast)
    }
}