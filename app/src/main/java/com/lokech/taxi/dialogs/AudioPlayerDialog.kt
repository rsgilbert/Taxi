package com.lokech.taxi.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.DialogFragment
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.ui.PlaybackControlView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.lokech.taxi.databinding.AudioPlayerDialogBinding
import timber.log.Timber

class AudioPlayerDialog : DialogFragment() {
    lateinit var playerView: PlaybackControlView
    lateinit var player: SimpleExoPlayer

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val binding = AudioPlayerDialogBinding.inflate(LayoutInflater.from(context))
            playerView = binding.playerView
            initializePlayer(arguments!!.getString("audioUrl")!!)
            builder.setView(binding.root)
            return builder.create()
        } ?: throw IllegalStateException("Activity can not be null")
    }

    // release the player to release system resources
    // guaranteed to run on all android apis, unlike onStop
    override fun onPause() {
        super.onPause()
        releasePlayer()
    }
}

private fun AudioPlayerDialog.initializePlayer(audioUrl: String) {
    player = ExoPlayerFactory.newSimpleInstance(context)
    playerView.player = player
    val uri = Uri.parse(audioUrl)
    val mediaSource = buildMediaSource(uri)
    player.playWhenReady = true
    player.prepare(mediaSource, true, true)
}

private fun AudioPlayerDialog.buildMediaSource(uri: Uri): MediaSource {
    val dataSourceFactory: DataSource.Factory =
        DefaultDataSourceFactory(context, "exoplayer-taxi")
    return ProgressiveMediaSource.Factory(dataSourceFactory)
        .createMediaSource(uri)
}


@SuppressLint("InlinedApi")
private fun AudioPlayerDialog.hideSystemUi() {
    playerView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
}

private fun AudioPlayerDialog.releasePlayer() {
    try {
        player.release()
        Timber.i("Resources released")
    } catch (e: Exception) {
        e.printStackTrace()
        Timber.i("release has an error: $e")
    }
}