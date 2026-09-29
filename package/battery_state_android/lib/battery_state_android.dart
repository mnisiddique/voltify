
import 'battery_state_android_platform_interface.dart';

class BatteryStateAndroid {
  Future<String?> getPlatformVersion() {
    return BatteryStateAndroidPlatform.instance.getPlatformVersion();
  }
}
