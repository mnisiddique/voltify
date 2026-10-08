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

    suspend fun getSettings(): Settings {
        return cacheSettings ?: datastore.data.first()[SETTINGS_JSON_KEY]?.let {
            cacheSettings = Settings.fromJson(it)
            cacheSettings
        } ?: defaultSettings
    }

    // Non-suspending read for callers that must not yield (e.g. per-broadcast handling).
    // Only accurate once getSettings() or saveSettings() has populated the cache.
    fun cachedSettings(): Settings = cacheSettings ?: defaultSettings
}
