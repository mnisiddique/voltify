package mni.siddique.battery_state_android.usecase.register

import mni.siddique.battery_state_android.settings.SettingsSrc

class RegisterBatteryStateReceiverUseCase(
    private val settingsSrc: SettingsSrc,
    private val currentBatteryLevelProvider: CurrentBatteryLevelProvider
) {

    private var isRegistered = false

    fun registerReceiver(onRegister: () -> Unit) {
        val threshold = settingsSrc.getSettings().threshold
        val currentLevel = currentBatteryLevelProvider.getBatteryLevel()
        if (!isRegistered && currentLevel < threshold) {
            onRegister()
            isRegistered = true
        }
    }

    fun unregisterReceiver(onUnregister: () -> Unit) {
        if (isRegistered) {
            onUnregister()
            isRegistered = false
        }
    }
}

/*
* Test
* 1. unregisterReceiverOnlyIfItWasRegistered
* 2. doesNotRegisterIfCurrentBatteryIsEqualOrGreaterThanThreshold
* 3. doesNotRegisterIfAlreadyRegistered
* 4. registersOnlyIfNotRegisteredAndCurrentBatteryLevelIsLessThanThreshold
* */