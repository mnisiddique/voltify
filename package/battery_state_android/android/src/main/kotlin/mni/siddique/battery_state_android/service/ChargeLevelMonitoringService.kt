package mni.siddique.battery_state_android.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import mni.siddique.battery_state_android.ServiceLocator
import mni.siddique.battery_state_android.StringResource
import mni.siddique.battery_state_android.usecase.ChargeLevelObserver
import mni.siddique.battery_state_android.usecase.ChargeState

class ChargeLevelMonitoringService(
    private val chargeLevelReceiver: ChargeLevelReceiver,
    private val chargeLevelObserver: ChargeLevelObserver,
) : Service() {

    constructor(): this(chargeLevelReceiver = ServiceLocator.getChargeLevelReceiver(this),)

    private val serviceScope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    companion object {
        const val CHANNEL_ID = StringResource.BATTERY_MONITOR_CHANNEL_ID
        private const val NOTIFICATION_ID = 1337
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
        serviceScope.launch {
            chargeLevelObserver.chargeState.collect { state ->
                when (state) {
                    is ChargeState.ThresholdReached -> {
                        // TODO: launch full screen intent to launch alarm activity
                    }

                    is ChargeState.Disconnected -> {
                        stopSelf()
                    }

                    else -> {}
                }
            }
        }
    }

    override fun onDestroy() {
        unregisterReceiver(chargeLevelReceiver)
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        startForeground(NOTIFICATION_ID, createStickyNotification())
        registerReceiver()
        return START_STICKY
    }

    private fun registerReceiver() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_BATTERY_CHANGED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
        }
        registerReceiver(chargeLevelReceiver, filter)
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                StringResource.BATTERY_MONITOR_SERVICE_NAME,
                NotificationManager.IMPORTANCE_LOW // Low importance so it's quiet in the notification shade
            ).apply {
                description = StringResource.BATTERY_MONITOR_CHANNEL_DESCRIPTION
            }
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }
    }

    private fun createStickyNotification(): Notification {
        // Intent to open your Flutter app if the user taps the notification
        val notificationIntent = packageManager.getLaunchIntentForPackage(packageName)
        val pendingIntent = PendingIntent.getActivity(
            this, 0, notificationIntent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle(StringResource.BATTERY_PROTECTION_TITLE)
            .setContentText(StringResource.MONITORING_CHARGE_LEVEL)
            .setSmallIcon(android.R.drawable.ic_lock_idle_charging) // Replace with your app's notification icon
            .setContentIntent(pendingIntent)
            .setOngoing(true) // Makes it sticky so the user knows it's running
            .build()
    }
}
