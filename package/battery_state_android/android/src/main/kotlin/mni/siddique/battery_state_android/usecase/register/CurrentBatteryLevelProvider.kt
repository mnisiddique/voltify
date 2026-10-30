package mni.siddique.battery_state_android.usecase.register

import android.content.Context
import android.os.BatteryManager

interface CurrentBatteryLevelProvider {
    fun getBatteryLevel(): Int
}

class CurrentBatteryLevelProviderImpl(private val appContext: Context) {
    fun getBatteryLevel(): Int {
        val batteryManager = appContext.getSystemService(
            BatteryManager::class.java
        )

        return batteryManager.getIntProperty(
            BatteryManager.BATTERY_PROPERTY_CAPACITY
        )
    }
}