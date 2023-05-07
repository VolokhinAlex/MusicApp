package com.volokhinaleksey.core.exoplayer

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.media3.common.Player
import androidx.media3.common.util.UnstableApi
import androidx.media3.session.MediaSession
import androidx.media3.session.MediaSessionService
import androidx.media3.ui.PlayerNotificationManager
import com.volokhinaleksey.core.utils.CHANNEL_ID
import com.volokhinaleksey.core.utils.NAME_NOTIFICATION
import com.volokhinaleksey.core.utils.NOTIFICATION_ID

class MusicNotificationManager(
    private val context: Context,
    private val player: Player
) {

    private var notificationManager: NotificationManagerCompat =
        NotificationManagerCompat.from(context)

    init {
        createNotificationChannel()
    }

    @UnstableApi
    fun startService(
        mediaSessionService: MediaSessionService,
        mediaSession: MediaSession
    ) {
        buildNotification(mediaSession)
        startForegroundNotification(mediaSessionService)
    }

    @UnstableApi
    private fun buildNotification(mediaSession: MediaSession) {
        PlayerNotificationManager.Builder(context, NOTIFICATION_ID, CHANNEL_ID)
            .setMediaDescriptionAdapter(
                MusicNotificationAdapter(
                    pendingIntent = mediaSession.sessionActivity
                )
            )
            .build()
            .apply {
                setMediaSessionToken(mediaSession.sessionCompatToken)
                setUseFastForwardActionInCompactView(true)
                setUseRewindActionInCompactView(true)
                setPriority(NotificationCompat.PRIORITY_DEFAULT)
                setUseNextActionInCompactView(false)
                setPlayer(player)
            }
    }

    private fun startForegroundNotification(mediaSessionService: MediaSessionService) {
        val notification = Notification.Builder(context, CHANNEL_ID)
            .setCategory(Notification.CATEGORY_SERVICE)
            .build()
        mediaSessionService.startForeground(NOTIFICATION_ID, notification)
    }

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            CHANNEL_ID,
            NAME_NOTIFICATION,
            NotificationManager.IMPORTANCE_DEFAULT
        )
        notificationManager.createNotificationChannel(channel)
    }

}