package org.example.app.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

/**
 * Simple repository that owns the ExoPlayer instance.
 */
class PlayerRepository(private val context: Context) {

    private var _player: ExoPlayer? = null
    val player: ExoPlayer
        get() {
            if (_player == null) {
                _player = ExoPlayer.Builder(context).build()
            }
            return _player!!
        }

    fun setMediaUri(uri: Uri) {
        val item = MediaItem.fromUri(uri)
        player.setMediaItem(item)
        player.prepare()
    }

    fun release() {
        _player?.release()
        _player = null
    }
}
