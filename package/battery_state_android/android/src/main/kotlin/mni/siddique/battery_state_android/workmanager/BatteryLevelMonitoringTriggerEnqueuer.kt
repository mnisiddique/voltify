package mni.siddique.battery_state_android.workmanager

interface BatteryLevelMonitoringTriggerEnqueuer {
    fun enqueueTrigger()
    fun dequeueTrigger()
}
/*
* BatteryLevelMonitoringQueue
* BatteryLevelMonitoringWork
* */