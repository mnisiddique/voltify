package mni.siddique.battery_state_android.usecase

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import mni.siddique.battery_state_android.settings.SettingsSrc

class ChargeLevelObserver(private val settingsSrc: SettingsSrc) {
    private val _chargeState = MutableStateFlow<ChargeState>(ChargeState.Initial)
    val chargeState: StateFlow<ChargeState> = _chargeState
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    fun observe(chargeLevel: Int, chargeScale: Int, event: SystemEvent) {
        when (event) {
            SystemEvent.BATTERY_CHANGED -> {
                scope.launch {
                    val percent = (chargeLevel * 100) / chargeScale
                    val setting = settingsSrc.getSettings()
                    if (percent >= setting.threshold) {
                        _chargeState.value = ChargeState.ThresholdReached(setting)
                    } else {
                        _chargeState.value = ChargeState.Charging
                    }
                }
            }
            SystemEvent.POWER_DISCONNECTED -> {
                _chargeState.value = ChargeState.Disconnected
            }
        }
    }
}