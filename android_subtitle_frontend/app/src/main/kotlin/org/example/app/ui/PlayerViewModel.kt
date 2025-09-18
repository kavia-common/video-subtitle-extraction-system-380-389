package org.example.app.ui

import android.app.Application
import android.graphics.Bitmap
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.exoplayer2.Player
import org.example.app.ml.SubtitleExtractor
import org.example.app.ml.TFLiteSubtitleExtractorStub
import org.example.app.player.PlayerRepository

/**
 PUBLIC_INTERFACE
 ViewModel exposing player state and live subtitle text.
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

            // Simulated frame capture: in a real app, capture a frame using VideoFrameMetadataListener
            // or a separate decoder path. Here we use a 1x1 dummy bitmap as placeholder.
            val fakeFrame = Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            val text = extractor.extractFromFrame(fakeFrame, p.currentPosition)
            if (!text.isNullOrBlank()) {
                _subtitle.value = text ?: ""
            }

            mainHandler.postDelayed(this, 500L)
        }
    }

    fun player() = repository.player

    fun startTicker() {
        stopTicker()
        mainHandler.post(ticker)
    }

    fun stopTicker() {
        mainHandler.removeCallbacks(ticker)
    }

    override fun onCleared() {
        super.onCleared()
        stopTicker()
        repository.release()
        extractor.release()
    }
}
