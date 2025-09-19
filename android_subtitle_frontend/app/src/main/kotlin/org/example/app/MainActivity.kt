package org.example.app

import android.Manifest
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.button.MaterialButton
import com.google.android.material.slider.Slider
import org.example.app.ui.PlayerViewModel
import org.example.app.util.TimeUtils

/**
 PUBLIC_INTERFACE
 Main entry activity hosting the video player, controls, and subtitle overlay.
 Summary:
 - Initializes ExoPlayer via the ViewModel.
 - Allows user to open a local MP4 using the system picker (SAF).
 - Controls playback (play/pause/stop) and seeking via a Material Slider.
 - Displays real-time subtitles produced by an integrated ML extractor (stubbed TFLite-ready).

 Parameters: None (entry activity)
 Returns: None
 Usage notes:
 - On API 33+ (Tiramisu), READ_EXTERNAL_STORAGE permission is not required for the SAF picker.
 - The ML extractor is a stub that simulates text at intervals; replace with a real model for production.
 */
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: PlayerViewModel
    private lateinit var playerView: PlayerView
    private lateinit var btnOpen: MaterialButton
    private lateinit var btnPlayPause: MaterialButton
    private lateinit var btnStop: MaterialButton
    private lateinit var seekBar: Slider
    private lateinit var txtCurrent: TextView
    private lateinit var txtDuration: TextView
    private lateinit var subtitleOverlay: TextView
    private lateinit var toolbar: MaterialToolbar

    // SAF content picker limited to "video/*" which includes MP4.
    private val pickVideoLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { loadVideo(it) }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel = ViewModelProvider(this)[PlayerViewModel::class.java]

        toolbar = findViewById(R.id.topAppBar)

        playerView = findViewById(R.id.playerView)
        btnOpen = findViewById(R.id.btnOpen)
        btnPlayPause = findViewById(R.id.btnPlayPause)
        btnStop = findViewById(R.id.btnStop)
        seekBar = findViewById(R.id.seekBar)
        txtCurrent = findViewById(R.id.txtCurrentTime)
        txtDuration = findViewById(R.id.txtDuration)
        subtitleOverlay = findViewById(R.id.subtitleOverlay)

        setupPlayer()
        setupControls()
        observeState()

        // Handle videos opened from other apps (e.g., share target)
        intent?.data?.let { loadVideo(it) }
    }

    private fun setupPlayer() {
        val player: ExoPlayer = viewModel.player()
        playerView.player = player
    }

    private fun setupControls() {
        btnOpen.setOnClickListener {
            ensureStoragePermissionThen {
                pickVideoLauncher.launch("video/*")
            }
        }
        btnPlayPause.setOnClickListener { togglePlayPause() }
        btnStop.setOnClickListener { stopPlayback() }
        seekBar.addOnChangeListener { _, value, fromUser ->
            if (fromUser) {
                val target = (value * (viewModel.durationMs.value ?: 0L) / 100f).toLong()
                viewModel.player().seekTo(target)
            }
        }
    }

    private fun observeState() {
        viewModel.subtitle.observe(this) { text ->
            subtitleOverlay.text =
                if (text.isNullOrBlank()) getString(R.string.subtitle_placeholder) else text
        }
        viewModel.positionMs.observe(this) { pos ->
            txtCurrent.text = TimeUtils.formatMs(pos)
            val dur = viewModel.durationMs.value ?: 0L
            if (dur > 0) {
                val ratio = pos.toFloat() / dur.toFloat()
                if (!seekBar.isPressed) {
                    seekBar.value = (ratio * 100f).coerceIn(0f, 100f)
                }
            }
        }
        viewModel.durationMs.observe(this) { dur ->
            txtDuration.text = TimeUtils.formatMs(dur)
        }
        viewModel.isPlaying.observe(this) { playing ->
            btnPlayPause.text =
                if (playing) getString(R.string.action_pause) else getString(R.string.action_play)
            if (playing) viewModel.startTicker() else viewModel.stopTicker()
        }
    }

    private fun togglePlayPause() {
        val player = viewModel.player()
        player.playWhenReady = !player.playWhenReady
    }

    private fun stopPlayback() {
        val player = viewModel.player()
        player.stop()
        player.clearMediaItems()
        subtitleOverlay.text = getString(R.string.subtitle_placeholder)
    }

    private fun loadVideo(uri: Uri) {
        val player = viewModel.player()
        player.setMediaItem(MediaItem.fromUri(uri))
        player.prepare()
        player.playWhenReady = true
    }

    private fun ensureStoragePermissionThen(onGranted: () -> Unit) {
        // For SDK 33+, READ_EXTERNAL_STORAGE is not needed for picking via SAF
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            onGranted()
            return
        }
        val perm = Manifest.permission.READ_EXTERNAL_STORAGE
        when {
            ContextCompat.checkSelfPermission(this, perm) == PackageManager.PERMISSION_GRANTED -> onGranted()
            shouldShowRequestPermissionRationale(perm) -> requestPermissionLauncher.launch(perm)
            else -> requestPermissionLauncher.launch(perm)
        }
    }

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { _ ->
            // No-op; flow continues via user action
        }

    override fun onStart() {
        super.onStart()
        viewModel.startTicker()
    }

    override fun onStop() {
        super.onStop()
        viewModel.stopTicker()
    }

    override fun onDestroy() {
        super.onDestroy()
        playerView.player = null
    }
}
