package mni.siddique.battery_state_android.usecase.texttospeech

import android.speech.tts.TextToSpeech
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mni.siddique.battery_state_android.settings.Settings
import mni.siddique.battery_state_android.usecase.texttospeech.provider.TextToSpeechProvider
import java.util.Locale
import kotlin.time.Duration.Companion.seconds

class SpeakAlarmText(
    private val ttsProvider: TextToSpeechProvider,
    private val settings: Settings,
    private val scope: CoroutineScope
) {
    private var textToSpeech: TextToSpeech? = null
    private var recurringJob: Job? = null

    suspend fun speak() {
        textToSpeech = textToSpeech ?: initFromSettings()

        recurringJob = scope.launch {
            while (recurringJob?.isActive == true) {
                textToSpeech?.speak(
                    settings.speech,
                    TextToSpeech.QUEUE_FLUSH,
                    null,
                    null
                )
                delay((settings.delay).seconds)
            }
        }
    }

    suspend fun initFromSettings(): TextToSpeech {
        val tts = ttsProvider.init()
        tts.language = Locale.forLanguageTag(settings.languageModel)
        tts.voice = tts.voices.find { voice ->
            voice.locale.language == settings.languageModel && voice.name.contains(
                settings.gender,
                ignoreCase = true
            )
        }
        return tts
    }

    fun stop() {
        recurringJob?.cancel()
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}

/*
* Test:
* 1. initializeTextToSpeechOnlyOncePerSpeakAlarmTextInstance
* 2. initializationDoneWithValidSettingsData
* 3. initializationDoNotFailsWithDefaultSettingsData
* 4. speakWorksIfInitFromSettingsInvokedBeforeInvokingSpeak
* 5. speakWorksIfInitFromSettingsInvokedAfterInvokingSpeak
* 6. speakKeepsRepeatingSettingsSpeechWithSettingsDelaySeconds
* */