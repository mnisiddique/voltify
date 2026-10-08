package mni.siddique.battery_state_android.usecase

import mni.siddique.battery_state_android.settings.Settings

sealed class ChargeState {
    data object Charging : ChargeState()
    data class ThresholdReached(val settings: Settings) : ChargeState()
    data object Disconnected : ChargeState()
}
