package mni.siddique.battery_state_android

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.MethodChannel.Result
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mni.siddique.battery_state_android.delegate.PluginDelegate
import mni.siddique.battery_state_android.delegate.PluginReply
import mni.siddique.battery_state_android.settings.SettingsSrc

/** BatteryStateAndroidPlugin */
class BatteryStateAndroidPlugin :
    FlutterPlugin,
    MethodCallHandler {
    // The MethodChannel that will the communication between Flutter and native Android
    //
    // This local reference serves to register the plugin with the Flutter Engine and unregister it
    // when the Flutter Engine is detached from the Activity
    private lateinit var channel: MethodChannel
    private lateinit var settingsSrc: SettingsSrc
    private lateinit var pluginDelegate: PluginDelegate
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    override fun onAttachedToEngine(flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        channel = MethodChannel(flutterPluginBinding.binaryMessenger, "battery_state_android")
        channel.setMethodCallHandler(this)
        ServiceLocator.init(flutterPluginBinding.applicationContext)
        settingsSrc = ServiceLocator.getSettingsRepo()
        pluginDelegate = ServiceLocator.getPluginDelegate()
    }

    override fun onMethodCall(
        call: MethodCall,
        result: Result
    ) {
        scope.launch {
            when (val reply = pluginDelegate.call(call.method, call.arguments.toString())) {
                is PluginReply.Success -> result.success(reply.value)
                is PluginReply.Error -> result.error(reply.code, reply.message, null)
                is PluginReply.NotImplemented -> result.notImplemented()
            }
        }
    }

    override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
        channel.setMethodCallHandler(null)
        ServiceLocator.clear()
    }
}


/*
* ObserveBatteryState
*   parse settings from flutter
*   if alert is on
*      enqueue work manager to get notified when charger plugged in
*
*   on charger plugged in
*      start foreground service
*      service will throw notification
*      if threshold reached
*         stop service
*         throw alarm notification
*
*    ObserveBatteryState
*       registerPowerConnectionEvent
*         - enqueWorkManager
*
*    Tasks
*      - WorkManagerInitializer -> will enqueue work manager
*      - ServiceInitializer -> will start service
*      - NotificationInitializer -> will start notification
*
* */