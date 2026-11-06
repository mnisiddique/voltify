package mni.siddique.battery_state_android.usecase.texttospeech.provider

import android.content.Context
import android.speech.tts.TextToSpeech
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resumeWithException

class TextToSpeechProviderImpl(
    private val context: Context
) : TextToSpeechProvider {

    override suspend fun init(): TextToSpeech = suspendCancellableCoroutine { continuation ->
        lateinit var tts: TextToSpeech
        tts = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                continuation.resume(tts) { cause, _, _ -> }
            } else {
                continuation.resumeWithException(
                    IllegalStateException("$GENERAL_MESSAGE ($status): ${msgByStatus(status)}")
                )
            }
        }
    }

    @PublishedApi
    internal fun msgByStatus(status: Int): String {
        return when (status) {
            TextToSpeech.ERROR -> ERROR
            TextToSpeech.ERROR_SYNTHESIS -> ERROR_SYNTHESIS
            TextToSpeech.ERROR_INVALID_REQUEST -> ERROR_INVALID_REQUEST
            TextToSpeech.ERROR_NETWORK -> ERROR_NETWORK
            TextToSpeech.ERROR_NETWORK_TIMEOUT -> ERROR_NETWORK_TIMEOUT
            TextToSpeech.ERROR_NOT_INSTALLED_YET -> ERROR_NOT_INSTALLED_YET
            TextToSpeech.ERROR_SERVICE -> ERROR_SERVICE
            else -> UNKNOWN_ERROR
        }
    }

    companion object {
        const val ERROR = "Generic initialization failure"
        const val ERROR_SYNTHESIS = "Missing language data or voice data package"
        const val ERROR_INVALID_REQUEST = "The request contains malformed parameters"
        const val ERROR_NETWORK = "Network failed while engine tries to connect to internet access"
        const val ERROR_NETWORK_TIMEOUT =
            "Connection timed out while trying to reach network resources"
        const val ERROR_NOT_INSTALLED_YET =
            "The requested voice or engine is still downloading/installing"
        const val ERROR_SERVICE = "The TTS service has died or is unreachable"
        const val UNKNOWN_ERROR = "Unknown error"
        const val GENERAL_MESSAGE = "TextToSpeech initialization failed. Status"
    }
}

/*
* Test
* 1. providesTextToSpeechInstanceOnSuccess
* 2. providesIllegalStateExceptionWithExpectedMessageOnInitializationFailure
*
*
* */