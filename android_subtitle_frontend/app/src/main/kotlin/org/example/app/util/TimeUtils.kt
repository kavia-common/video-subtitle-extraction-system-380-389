package org.example.app.util

// PUBLIC_INTERFACE
object TimeUtils {
    /** Format milliseconds to mm:ss string. */
    fun formatMs(ms: Long): String {
        if (ms <= 0) return "00:00"
        val totalSeconds = ms / 1000
        val minutes = totalSeconds / 60
        val seconds = totalSeconds % 60
        return String.format("%02d:%02d", minutes, seconds)
    }
}
