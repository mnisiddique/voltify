package mni.siddique.battery_state_android.usecase.batterystate


sealed class VoltifyBatteryState {
    data object Idle : VoltifyBatteryState()
    data object ThresholdReached : VoltifyBatteryState()
    data object Disconnected : VoltifyBatteryState()
}
