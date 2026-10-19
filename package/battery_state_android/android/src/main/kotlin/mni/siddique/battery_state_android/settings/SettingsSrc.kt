package mni.siddique.battery_state_android.settings

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first


internal val Context.dataStore by preferencesDataStore(name = "battery_state_android_prefs")

class SettingsSrc(private val datastore: DataStore<Preferences>) {

    @Volatile
    private var cacheSettings: Settings? = null

    companion object {
        val SETTINGS_JSON_KEY = stringPreferencesKey("settings_json")
    }

    suspend fun saveSettings(settingsJson: String) {
        cacheSettings = Settings.fromJson(settingsJson)
        datastore.edit {
            it[SETTINGS_JSON_KEY] = settingsJson
        }
    }

    suspend fun preWarmCache() {
        cacheSettings = datastore.data.first()[SETTINGS_JSON_KEY]?.let {
            cacheSettings = Settings.fromJson(it)
            cacheSettings
        } ?: defaultSettings
    }

    fun getSettings(): Settings {
        return cacheSettings ?: throw IllegalStateException("Settings cache not initialized")
    }
}
