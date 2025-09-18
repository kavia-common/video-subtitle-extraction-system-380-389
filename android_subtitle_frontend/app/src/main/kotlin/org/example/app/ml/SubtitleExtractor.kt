package org.example.app.ml

import android.content.Context
import android.graphics.Bitmap

// PUBLIC_INTERFACE
interface SubtitleExtractor {
    /** Extracts subtitle text from a frame (bitmap) and current playback position in ms. */
    fun extractFromFrame(frame: Bitmap, positionMs: Long): String?
    /** Release resources tied to the ML engine. */
    fun release()
}

/**
 * Stub implementation that simulates on-device inference. This class is designed for
 * drop-in replacement with a real TFLite pipeline. To integrate a real model:
 * - Load a TFLite model from assets (e.g., assets/model.tflite)
 * - Preprocess the bitmap into an input tensor
 * - Run interpreter.run(input, output)
 * - Postprocess output to text
 */
class TFLiteSubtitleExtractorStub(context: Context) : SubtitleExtractor {

    private var warm = false

    override fun extractFromFrame(frame: Bitmap, positionMs: Long): String? {
        // Fake warm-up and simulated subtitle fragments every ~2 seconds.
        if (!warm) {
            warm = true
            return null
        }
        val seconds = (positionMs / 1000).toInt()
        return when {
            seconds % 7 == 0 -> "Exploring the ocean of knowledge."
            seconds % 5 == 0 -> "Machine learning listens to the video."
            seconds % 3 == 0 -> "Real-time subtitles are appearing."
            else -> null
        }
    }

    override fun release() {
        // No-op for stub
    }
}
