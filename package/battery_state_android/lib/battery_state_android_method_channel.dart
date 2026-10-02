import 'package:battery_state_android/settings.dart';
import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'battery_state_android_platform_interface.dart';

/// An implementation of [BatteryStateAndroidPlatform] that uses method channels.
class MethodChannelBatteryStateAndroid extends BatteryStateAndroidPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('battery_state_android');

  @override
  Future<void> observeBatteryState(Settings settings) async {
    methodChannel.invokeMethod('observeBatteryState', settings.toMap());
  }
  @override
  Future<Settings> getSettings() async {
    final settingsMap = await methodChannel.invokeMethod('getSettings');
    return Settings.fromMap(settingsMap);
  }
}
