import 'package:battery_state_android/settings.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'battery_state_android_method_channel.dart';

abstract class BatteryStateAndroidPlatform extends PlatformInterface {
  /// Constructs a BatteryStateAndroidPlatform.
  BatteryStateAndroidPlatform() : super(token: _token);

  static final Object _token = Object();

  static BatteryStateAndroidPlatform _instance =
      MethodChannelBatteryStateAndroid();

  /// The default instance of [BatteryStateAndroidPlatform] to use.
  ///
  /// Defaults to [MethodChannelBatteryStateAndroid].
  static BatteryStateAndroidPlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [BatteryStateAndroidPlatform] when
  /// they register themselves.
  static set instance(BatteryStateAndroidPlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<void> observeBatteryState(Settings settings);
  Future<Settings> getSettings();
}
