package app.lawnchair.smartspace.provider

import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Handler
import android.os.Looper
import app.lawnchair.smartspace.model.SmartspaceAction
import app.lawnchair.smartspace.model.SmartspaceScores
import app.lawnchair.smartspace.model.SmartspaceTarget
import com.android.launcher3.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transformLatest

/**
 * Graphite: the song Now Playing recognised nearby, as on the Pixel's At a Glance. Graphite Now Playing decides when a
 * song is shown, the same way Ambient Music Mod's lock screen overlay did (until the track's estimated end), and this
 * shows it until then. Tapping opens the Now Playing history.
 */
class RecognisedSongProvider(context: Context) :
    SmartspaceDataSource(
        context,
        R.string.smartspace_recognised_song,
        { smartspaceRecognisedSong },
    ) {

    private val songUri = Uri.parse("content://org.graphite.nowplaying.api/song")
    private val apiUri = Uri.parse("content://org.graphite.nowplaying.api")

    override val isAvailable = context.packageManager.resolveContentProvider("org.graphite.nowplaying.api", 0) != null

    private val changes = callbackFlow {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                trySend(Unit)
            }
        }
        runCatching { context.contentResolver.registerContentObserver(songUri, false, observer) }
        trySend(Unit)
        awaitClose { context.contentResolver.unregisterContentObserver(observer) }
    }

    override val internalTargets = changes.transformLatest {
        val song = runCatching { context.contentResolver.call(apiUri, "getSong", null, null) }.getOrNull()
        val track = song?.getString("track")
        val endTime = song?.getLong("endTime") ?: 0L
        val remaining = endTime - System.currentTimeMillis()
        if (track == null || remaining <= 0) {
            emit(emptyList())
            return@transformLatest
        }
        emit(listOf(target(track, song.getString("artist"))))
        // Gone when the song is estimated to have finished, unless Now Playing announces something first
        delay(remaining)
        emit(emptyList())
    }.flowOn(Dispatchers.IO)

    private fun target(track: String, artist: String?) = SmartspaceTarget(
        id = "graphiteRecognisedSong",
        headerAction = SmartspaceAction(
            id = "graphiteRecognisedSongAction",
            icon = Icon.createWithResource(context, R.drawable.ic_smartspace_recognised_song),
            title = track,
            subtitle = artist,
            intent = Intent("com.google.intelligence.sense.NOW_PLAYING_HISTORY")
                .setPackage("com.kieronquinn.app.pixelambientmusic")
                .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
        ),
        score = SmartspaceScores.SCORE_MEDIA,
        featureType = SmartspaceTarget.FeatureType.FEATURE_MEDIA,
    )
}
