## AndroidBatteryStateInterface.dart
observeBatteryState(settingsModel)
getSettings()

## Settings.dart
   - threshold: Int
   - speech: String
   - languageModel: String
   - delay: Int
   - isAlertOn: Boolean

**Note** Settings will be passed to native side and native side will store them to datastore 
and also retrieve them during usage.

## SettingsRepo.kt
- saveSettings(mode: Settings)
- getCurrentSettings()
- getThreshold()
- getSpeech()
- getLanguageModel()
- getDelay()
- getAlertOnFlag()


## BatteryStateHandler.kt
   - thresholdReached(action: String?, level: Int?, scale: Int?) : Boolean