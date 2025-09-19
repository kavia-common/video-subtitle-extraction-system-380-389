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
 PUBLIC_INTERFACE
 Stub implementation that simulates on-device inference. Designed for
 drop-in replacement with a real TFLite pipeline.

 To integrate a real model:
 - Place model.tflite under app/src/main/assets/
 - Initialize Interpreter with MappedByteBuffer from assets
 - Preprocess the bitmap into an input tensor (e.g., RGB bytebuffer / float32)
 - interpreter.run(input, output)
 - Convert output to readable subtitle text

 Returns: Simulated strings at intervals to emulate inference cadence.
 */
class TFLiteSubtitleExtractorStub(context: Context) : SubtitleExtractor {

    private var warm = false

    override fun extractFromFrame(frame: Bitmap, positionMs: Long): String? {
        // Fake warm-up and simulated subtitle fragments every ~0.5s checks.
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
