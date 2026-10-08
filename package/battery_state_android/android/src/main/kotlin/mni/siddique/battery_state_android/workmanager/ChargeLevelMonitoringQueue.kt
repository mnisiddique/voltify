package mni.siddique.battery_state_android.workmanager

interface ChargeLevelMonitoringQueue {
    fun enqueueChargeLevelMonitoring()
    fun cancelChargeLevelMonitoring()
}
