package org.example.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.example.app.ml.SubtitleExtractor
import org.example.app.ml.TFLiteSubtitleExtractorStub
import org.example.app.player.PlayerRepository

/**
 PUBLIC_INTERFACE
 ViewModel exposing player state and live subtitle text.
 Responsibilities:
 - Owns PlayerRepository for ExoPlayer lifecycle.
 - Polls playback position and duration.
 - Periodically invokes SubtitleExtractor with a placeholder frame to simulate ML inference.

 LiveData:
 - subtitle: Latest subtitle line for the UI overlay.
 - durationMs: Total media duration in ms.
 - positionMs: Current playback position in ms.
 - isPlaying: Whether the player is currently playing.
 */
class PlayerViewModel(app: Application) : AndroidViewModel(app) {

    private val repository = PlayerRepository(app)
    private val extractor: SubtitleExtractor = TFLiteSubtitleExtractorStub(app)

    private val _subtitle = MutableLiveData<String>("")
    val subtitle: LiveData<String> = _subtitle

    private val _durationMs = MutableLiveData<Long>(0L)
    val durationMs: LiveData<Long> = _durationMs

    private val _positionMs = MutableLiveData<Long>(0L)
    val positionMs: LiveData<Long> = _positionMs

    private val _isPlaying = MutableLiveData<Boolean>(false)
    val isPlaying: LiveData<Boolean> = _isPlaying

    private val mainHandler = Handler(Looper.getMainLooper())
    private val ticker = object : Runnable {
        override fun run() {
            val p = repository.player
            _positionMs.value = p.currentPosition
            _durationMs.value = p.duration
            _isPlaying.value = p.isPlaying

            // NOTE: In production, capture real frames via ExoPlayer video frame APIs or a decoder pipeline.
            val fakeFrame = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            val text = extractor.extractFromFrame(fakeFrame, p.currentPosition)
            if (!text.isNullOrBlank()) {
                // Lint: enforce non-null assignment for LiveData value
                _subtitle.value = text ?: ""
            }

            mainHandler.postDelayed(this, 500L)
        }
    }

    // PUBLIC_INTERFACE
    fun player() = repository.player

    // PUBLIC_INTERFACE
    fun startTicker() {
        /** Starts periodic updates of playback state and ML extraction. */
        stopTicker()
        mainHandler.post(ticker)
    }

    // PUBLIC_INTERFACE
    fun stopTicker() {
        /** Stops periodic updates. Call in onStop/onCleared to avoid leaks. */
        mainHandler.removeCallbacks(ticker)
    }

    override fun onCleared() {
        super.onCleared()
        stopTicker()
        repository.release()
        extractor.release()
    }
}
