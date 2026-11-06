package mni.siddique.battery_state_android.usecase.texttospeech.provider

import android.speech.tts.TextToSpeech

interface TextToSpeechProvider {
    suspend fun init() : TextToSpeech
}