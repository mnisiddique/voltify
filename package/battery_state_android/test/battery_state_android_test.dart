import 'package:flutter_test/flutter_test.dart';
import 'package:battery_state_android/battery_state_android.dart';
import 'package:battery_state_android/battery_state_android_platform_interface.dart';
import 'package:battery_state_android/battery_state_android_method_channel.dart';
import 'package:plugin_platform_interface/plugin_platform_interface.dart';

class MockBatteryStateAndroidPlatform
    with MockPlatformInterfaceMixin
    implements BatteryStateAndroidPlatform {
  @override
  Future<String?> getPlatformVersion() => Future.value('42');
}

void main() {
  final BatteryStateAndroidPlatform initialPlatform = BatteryStateAndroidPlatform.instance;

  test('$MethodChannelBatteryStateAndroid is the default instance', () {
    expect(initialPlatform, isInstanceOf<MethodChannelBatteryStateAndroid>());
  });

  test('getPlatformVersion', () async {
    BatteryStateAndroid batteryStateAndroidPlugin = BatteryStateAndroid();
    MockBatteryStateAndroidPlatform fakePlatform = MockBatteryStateAndroidPlatform();
    BatteryStateAndroidPlatform.instance = fakePlatform;

    expect(await batteryStateAndroidPlugin.getPlatformVersion(), '42');
  });
}
