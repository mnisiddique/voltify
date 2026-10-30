package mni.siddique.battery_state_android.usecase.batterystate

import android.hardware.BatteryState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import mni.siddique.battery_state_android.settings.SettingsSrc

class AnalyzeBatteryStateUseCase(
    private val settingsSrc: SettingsSrc
) {

    private val _batteryState = MutableStateFlow<VoltifyBatteryState>(VoltifyBatteryState.Idle)

    val batteryState: StateFlow<VoltifyBatteryState> = _batteryState

    fun analyze(batteryStateBroadCast: BatteryStateBroadCast) {
        when (batteryStateBroadCast.event) {
            SystemEvent.BATTERY_CHANGED -> {
                val level = calculateBatteryLevel(batteryStateBroadCast)
                val setting = settingsSrc.getSettings()
                if (level >= setting.threshold) {
                    emit(VoltifyBatteryState.ThresholdReached)
                }
            }

            SystemEvent.POWER_DISCONNECTED -> emit(VoltifyBatteryState.Disconnected)
            else -> {}
        }
    }

    fun calculateBatteryLevel(batteryStateBroadCast: BatteryStateBroadCast): Int {
        val level = batteryStateBroadCast.batteryLevel
        val scale = batteryStateBroadCast.batteryScale
        return (level * 100) / scale
    }

    fun isEmittable(currentState: VoltifyBatteryState): Boolean =
        when (_batteryState.value) {
            is VoltifyBatteryState.Idle -> true
            else -> currentState != _batteryState.value
        }

    fun emit(state: VoltifyBatteryState) {
        if (isEmittable(state)) {
            _batteryState.value = state
        }
    }
}

/* Tests
* 1. calculateBatteryLevelPercentageCorrectly
* 2. doesNotEmitDuplicateState
* 3. multipleRandomAnalyzeCallDoesNotMutateStateRandomly
* */
