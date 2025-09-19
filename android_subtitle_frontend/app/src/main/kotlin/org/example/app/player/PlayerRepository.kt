package org.example.app.player

import android.content.Context
import android.net.Uri
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem

/**
 PUBLIC_INTERFACE
 Repository that owns and lazily creates a single ExoPlayer instance.
 Notes:
 - Keeps player lifecycle centralized so ViewModel/Activity can share.
 - Use setMediaUri for convenience when not configuring the item externally.
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

    // PUBLIC_INTERFACE
    fun setMediaUri(uri: Uri) {
        /** Sets a media item from the provided URI and prepares the player. */
        val item = MediaItem.fromUri(uri)
        player.setMediaItem(item)
        player.prepare()
    }

    // PUBLIC_INTERFACE
    fun release() {
        /** Releases the underlying ExoPlayer instance to free resources. */
        _player?.release()
        _player = null
    }
}
