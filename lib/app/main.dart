import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:voltify/screens/battery_threshold_alert_screen.dart';

void main() {
  WidgetsFlutterBinding.ensureInitialized();

  // Set system UI overlay style for dark immersive alert theme
  SystemChrome.setSystemUIOverlayStyle(
    const SystemUiOverlayStyle(
      statusBarColor: Colors.transparent,
      statusBarIconBrightness: Brightness.light,
      systemNavigationBarColor: Color(0xFF0C1017),
      systemNavigationBarIconBrightness: Brightness.light,
    ),
  );

  runApp(const VoltifyApp());
}

class VoltifyApp extends StatelessWidget {
  const VoltifyApp({super.key});

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Voltify',
      debugShowCheckedModeBanner: false,
      theme: ThemeData(
        brightness: Brightness.dark,
        scaffoldBackgroundColor: const Color(0xFF0C1017),
        colorScheme: ColorScheme.fromSeed(
          seedColor: const Color(0xFF10B981),
          brightness: Brightness.dark,
        ),
        useMaterial3: true,
      ),
      home: const SettingsScreen(batteryLevel: 80, thresholdLevel: 80),
    );
  }
}
