package mni.siddique.battery_state_android.usecase.texttospeech.voiceinfo

import mni.siddique.battery_state_android.usecase.texttospeech.provider.TextToSpeechProvider

class GetVoiceInfo(
    private val ttsProvider: TextToSpeechProvider
) {
    suspend operator fun invoke(): List<VoiceInfo> {
        val tts = ttsProvider.init()
        val voices = tts.voices.filter { voice -> !voice.isNetworkConnectionRequired }
            .map { voice -> VoiceInfo.fromTtsVoice(voice) }
        return voices
    }
}

/*
* Test:
* 1. providesListOfVoiceInfoWhoDoNotRequireNetworkConnection
* */