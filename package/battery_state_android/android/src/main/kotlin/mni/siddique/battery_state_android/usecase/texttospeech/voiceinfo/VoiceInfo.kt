package mni.siddique.battery_state_android.usecase.texttospeech.voiceinfo

import android.annotation.SuppressLint
import android.speech.tts.Voice
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@SuppressLint("UnsafeOptInUsageError")
@Serializable
data class VoiceInfo(
    val voiceName: String,
    val language: String,
    val gender: String
) {
    fun toJson(): String = json.encodeToString(serializer(), this)

    companion object {
        private val json = Json { ignoreUnknownKeys = true }
        const val MALE = "male"
        fun fromTtsVoice(voice: Voice): VoiceInfo = VoiceInfo(
            voiceName = voice.name,
            language = voice.locale.language,
            gender = when {
                voice.name.contains(MALE, ignoreCase = true) -> "Male"
                else -> "Female"
            }
        )

    }
}
