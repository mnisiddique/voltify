package mni.siddique.battery_state_android

import android.annotation.SuppressLint
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json


@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class Settings(
    val threshold: Int,
    val speech: String,
    val languageModel: String,
    val delay: Int,
    val isAlertOn: Boolean,
) {
    fun toJson(): String = json.encodeToString(serializer(), this)

    companion object {
        private val json = Json { ignoreUnknownKeys = true }

        fun fromJson(jsonString: String): Settings =
            json.decodeFromString(serializer(), jsonString)
    }
}

val defaultSettings = Settings(
    threshold = 100,
    speech = "Your battery is at 100%. Please plug out",
    languageModel = "en",
    delay = 1,
    isAlertOn = true,
)
