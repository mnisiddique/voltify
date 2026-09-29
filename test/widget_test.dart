import 'package:flutter/material.dart';
import 'package:flutter_test/flutter_test.dart';
import 'package:voltify/main.dart';
import 'package:voltify/screens/battery_threshold_alert_screen.dart';

void main() {
  testWidgets('Threshold alert screen renders correctly at launch',
      (WidgetTester tester) async {
    await tester.pumpWidget(const VoltifyApp());
    await tester.pump();

    // Verify key titles and instructions are present
    expect(find.text('Battery Threshold Reached'), findsOneWidget);
    expect(find.text('ALERT: CHARGE LIMIT REACHED'), findsOneWidget);
    expect(find.text('80'), findsOneWidget);
    expect(find.text('Dismiss Alert'), findsOneWidget);
    expect(
      find.text('Unplug the charger now to complete charging cycle.'),
      findsOneWidget,
    );
  });

  testWidgets('Dismiss button updates state or invokes onDismiss',
      (WidgetTester tester) async {
    bool dismissedCalled = false;

    await tester.pumpWidget(
      MaterialApp(
        home: BatteryThresholdAlertScreen(
          batteryLevel: 85,
          thresholdLevel: 85,
          onDismiss: () {
            dismissedCalled = true;
          },
        ),
      ),
    );
    await tester.pump();

    expect(find.text('85'), findsOneWidget);
    expect(find.text('Dismiss Alert'), findsOneWidget);

    await tester.tap(find.text('Dismiss Alert'));
    await tester.pump(const Duration(milliseconds: 350));

    expect(dismissedCalled, isTrue);
  });

  testWidgets('Default dismiss shows confirmation view and allows reset',
      (WidgetTester tester) async {
    await tester.pumpWidget(
      const MaterialApp(
        home: BatteryThresholdAlertScreen(
          batteryLevel: 80,
          thresholdLevel: 80,
        ),
      ),
    );
    await tester.pump();

    expect(find.text('Dismiss Alert'), findsOneWidget);
    await tester.tap(find.text('Dismiss Alert'));
    await tester.pump(const Duration(milliseconds: 350));

    expect(find.text('Alert Dismissed'), findsOneWidget);
    expect(find.text('Preview Again'), findsOneWidget);

    await tester.tap(find.text('Preview Again'));
    await tester.pump(const Duration(milliseconds: 350));

    expect(find.text('Battery Threshold Reached'), findsOneWidget);
  });

  testWidgets('Works cleanly in landscape mode without overflow',
      (WidgetTester tester) async {
    // Set landscape screen dimensions (e.g. 844 x 390 - iPhone 14 landscape)
    tester.view.physicalSize = const Size(844, 390);
    tester.view.devicePixelRatio = 1.0;
    addTearDown(tester.view.resetPhysicalSize);
    addTearDown(tester.view.resetDevicePixelRatio);

    await tester.pumpWidget(
      const MaterialApp(
        home: BatteryThresholdAlertScreen(
          batteryLevel: 90,
          thresholdLevel: 90,
        ),
      ),
    );
    await tester.pump();

    expect(find.text('Battery Threshold Reached'), findsOneWidget);
    expect(find.text('90'), findsOneWidget);
    expect(find.text('Dismiss Alert'), findsOneWidget);
  });

  testWidgets('Works cleanly on compact screen (iPhone SE / small Android)',
      (WidgetTester tester) async {
    tester.view.physicalSize = const Size(320, 568);
    tester.view.devicePixelRatio = 1.0;
    addTearDown(tester.view.resetPhysicalSize);
    addTearDown(tester.view.resetDevicePixelRatio);

    await tester.pumpWidget(
      const MaterialApp(
        home: BatteryThresholdAlertScreen(
          batteryLevel: 80,
          thresholdLevel: 80,
        ),
      ),
    );
    await tester.pump();

    expect(find.text('Battery Threshold Reached'), findsOneWidget);
    expect(find.text('80'), findsOneWidget);
    expect(find.text('Dismiss Alert'), findsOneWidget);
  });
}
