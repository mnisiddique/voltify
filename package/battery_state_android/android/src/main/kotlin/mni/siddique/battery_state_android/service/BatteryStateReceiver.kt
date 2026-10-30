package mni.siddique.battery_state_android.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import mni.siddique.battery_state_android.usecase.batterystate.AnalyzeBatteryStateUseCase
import mni.siddique.battery_state_android.usecase.batterystate.BatteryStateBroadCast

class BatteryStateReceiver(
    private val analyzeBatteryStateUseCase: AnalyzeBatteryStateUseCase
) : BroadcastReceiver() {
    override fun onReceive(ctx: Context?, intent: Intent?) {
        if (intent == null) return
        val batteryStateBroadCast = BatteryStateBroadCast.fromIntent(intent)
        analyzeBatteryStateUseCase.analyze(batteryStateBroadCast)
    }
}