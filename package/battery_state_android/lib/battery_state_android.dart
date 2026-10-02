
import 'package:battery_state_android/settings.dart';

import 'battery_state_android_platform_interface.dart';

class BatteryStateAndroid {
  Future<Settings> getSettings() {
    return BatteryStateAndroidPlatform.instance.getSettings();
  }

  Future<void> observeBatteryState(Settings settings) {
    return BatteryStateAndroidPlatform.instance.observeBatteryState(settings);
  }
}
