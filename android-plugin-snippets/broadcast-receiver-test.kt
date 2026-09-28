package com.napcosecurity.fireburg

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.napcosecurity.fireburg.ui.common.HideSystemBars
import com.napcosecurity.fireburg.ui.common.TextToSpeechWidget
import com.napcosecurity.fireburg.ui.navigation.FireBurgNavHost
import com.napcosecurity.fireburg.ui.newshell.viewmodel.NapcoPanelViewModel
import com.napcosecurity.fireburg.ui.newshell.viewmodel.WifiMonitorViewModel
import com.napcosecurity.fireburg.ui.theme.FireBurgTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val napcoViewModel: NapcoPanelViewModel by viewModels()
    private val wifiMonitorViewModel: WifiMonitorViewModel by viewModels()
    private lateinit var batteryStateReceiver: BatteryStateReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        batteryStateReceiver = BatteryStateReceiver()

        setContent {
            BatteryStateCompose(batteryStateReceiver.batteryState)
//            FireBurgTheme {
//                TextToSpeechWidget(
//                    content = {
//                        HideSystemBars()
//                        FireBurgNavHost(
//                            napcoViewModel = napcoViewModel,
//                            wifiMonitorViewModel = wifiMonitorViewModel
//                        )
//                    })
//            }
        }
    }

    override fun onResume() {
        super.onResume()

        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_POWER_CONNECTED)
            addAction(Intent.ACTION_POWER_DISCONNECTED)
            addAction(Intent.ACTION_BATTERY_CHANGED)
        }
        registerReceiver(batteryStateReceiver, filter)
    }

    override fun onPause() {
        super.onPause()
        unregisterReceiver(batteryStateReceiver)
    }
}


sealed class BatteryState {
    data class PowerConnected(private val chargeLevel: Int?, private val chargeScale: Int?) :
        BatteryState() {
        val chargeLevelInPercent
            get() = if (chargeLevel != null && chargeScale != null) {
                chargeLevel * 100 / chargeScale
            } else {
                null
            }
    }

    object PowerDisconnected : BatteryState()
}


class BatteryStateReceiver : BroadcastReceiver() {
    private val _batteryState = MutableStateFlow<BatteryState>(BatteryState.PowerDisconnected)
    val batteryState : StateFlow<BatteryState> = _batteryState
    override fun onReceive(ctx: Context?, intent: Intent?) {
        when (intent?.action) {
            Intent.ACTION_POWER_CONNECTED -> {
                _batteryState.value = BatteryState.PowerConnected(null, null)
            }

            Intent.ACTION_POWER_DISCONNECTED -> {
                _batteryState.value = BatteryState.PowerDisconnected
            }

            Intent.ACTION_BATTERY_CHANGED -> {
                when (_batteryState.value) {
                    is BatteryState.PowerConnected -> {
                        val level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1)
                        val scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1)
                        if (level != -1 && scale != -1) {
                            _batteryState.value = BatteryState.PowerConnected(level, scale)
                        }
                    }

                    else -> {}
                }

            }

        }
    }

}

@Composable
fun BatteryStateCompose(batteryStateFlow: StateFlow<BatteryState>) {
    val state by batteryStateFlow.collectAsStateWithLifecycle()
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            when (state) {
                is BatteryState.PowerConnected -> {
                    val chargeLevel = (state as BatteryState.PowerConnected).chargeLevelInPercent
                    Text(text = "Battery State: connected")
                    Text(text = "Charge: ${chargeLevel ?: ""}%")
                }

                is BatteryState.PowerDisconnected -> {
                    Text(text = "Battery State: Disconnected")
                }
            }
        }
    }
}