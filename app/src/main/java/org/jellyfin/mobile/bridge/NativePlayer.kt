package org.jellyfin.mobile.bridge

import android.content.Intent
import android.webkit.JavascriptInterface
import kotlinx.coroutines.channels.Channel
import org.jellyfin.mobile.MainActivity
import org.jellyfin.mobile.PLAYER_EVENT_CHANNEL
import org.jellyfin.mobile.player.ExoPlayerFormats
import org.jellyfin.mobile.player.PlayerActivity
import org.jellyfin.mobile.player.PlayerEvent
import org.jellyfin.mobile.settings.VideoPlayerType
import org.jellyfin.mobile.utils.Constants
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.qualifier.named

class NativePlayer(private val activity: MainActivity) : KoinComponent {

    private val playerEventChannel: Channel<PlayerEvent> by inject(named(PLAYER_EVENT_CHANNEL))

    @JavascriptInterface
    fun isEnabled() = activity.appPreferences.videoPlayerType == VideoPlayerType.EXO_PLAYER

    @JavascriptInterface
    fun getSupportedFormats() = ExoPlayerFormats.supportedCodecs.toJSONString()

    @JavascriptInterface
    fun loadPlayer(args: String) {
        val playerIntent = Intent(activity, PlayerActivity::class.java).apply {
            action = Constants.ACTION_PLAY_MEDIA
            putExtra(Constants.EXTRA_MEDIA_SOURCE_ITEM, args)
        }
        activity.startActivity(playerIntent)
    }

    @JavascriptInterface
    fun pausePlayer() {
        playerEventChannel.offer(PlayerEvent.Pause)
    }

    @JavascriptInterface
    fun resumePlayer() {
        playerEventChannel.offer(PlayerEvent.Resume)
    }

    @JavascriptInterface
    fun stopPlayer() {
        playerEventChannel.offer(PlayerEvent.Stop)
    }

    @JavascriptInterface
    fun destroyPlayer() {
        playerEventChannel.offer(PlayerEvent.Destroy)
    }

    @JavascriptInterface
    fun seek(ticks: Long) {
        playerEventChannel.offer(PlayerEvent.Seek(ticks / Constants.TICKS_PER_MILLISECOND))
    }

    @JavascriptInterface
    fun seekMs(ms: Long) {
        playerEventChannel.offer(PlayerEvent.Seek(ms))
    }

    @JavascriptInterface
    fun setVolume(volume: Int) {
        playerEventChannel.offer(PlayerEvent.SetVolume(volume))
    }
}
