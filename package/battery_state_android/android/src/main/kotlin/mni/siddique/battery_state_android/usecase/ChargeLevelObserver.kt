package mni.siddique.battery_state_android.usecase

import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import mni.siddique.battery_state_android.settings.SettingsSrc

class ChargeLevelObserver(private val settingsSrc: SettingsSrc) {
    // Events, not state: no replay, so a new collector never sees a previous session's
    // Disconnected/ThresholdReached.
    private val _chargeState = MutableSharedFlow<ChargeState>(
        replay = 0,
        extraBufferCapacity = 16,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val chargeState: SharedFlow<ChargeState> = _chargeState

    private var lastState: ChargeState? = null

    /** Forget the last emitted state; call when a new monitoring session starts. */
    fun reset() {
        lastState = null
    }

    // Synchronous on purpose: broadcasts arrive in order on the main thread, and handling
    // them without a suspension point keeps BATTERY_CHANGED from being reordered after
    // POWER_DISCONNECTED. Settings come from SettingsSrc's in-memory cache.
    fun observe(chargeLevel: Int, chargeScale: Int, event: SystemEvent) {
        val state = when (event) {
            SystemEvent.BATTERY_CHANGED -> {
                val percent = (chargeLevel * 100) / chargeScale
                val setting = settingsSrc.cachedSettings()
                if (percent >= setting.threshold) ChargeState.ThresholdReached(setting)
                else ChargeState.Charging
            }
            SystemEvent.POWER_DISCONNECTED -> ChargeState.Disconnected
        }
        // BATTERY_CHANGED also fires for voltage/temperature changes; only emit transitions.
        if (state == lastState) return
        lastState = state
        _chargeState.tryEmit(state)
    }
}
