import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'battery_state_android_platform_interface.dart';

/// An implementation of [BatteryStateAndroidPlatform] that uses method channels.
class MethodChannelBatteryStateAndroid extends BatteryStateAndroidPlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('battery_state_android');

  @override
  Future<String?> getPlatformVersion() async {
    final version = await methodChannel.invokeMethod<String>(
      'getPlatformVersion',
    );
    return version;
  }
}
