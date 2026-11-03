package app.lawnchair.smartspace.provider

import android.Manifest
import android.content.ContentUris
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.PackageManager
import android.graphics.drawable.Icon
import android.provider.CalendarContract
import android.text.format.DateFormat
import app.lawnchair.smartspace.model.SmartspaceAction
import app.lawnchair.smartspace.model.SmartspaceScores
import app.lawnchair.smartspace.model.SmartspaceTarget
import app.lawnchair.util.broadcastReceiverFlow
import com.android.launcher3.R
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/**
 * Graphite: the next calendar event in the coming hours, as on the Pixel's At a Glance. Reads the
 * calendar on the device only, and shows nothing without the calendar permission.
 */
class CalendarEventProvider(context: Context) :
    SmartspaceDataSource(
        context,
        R.string.smartspace_calendar_events,
        { smartspaceCalendarEvents },
    ) {

    override val internalTargets = broadcastReceiverFlow(
        context,
        IntentFilter(Intent.ACTION_TIME_TICK).apply { addAction(Intent.ACTION_PROVIDER_CHANGED) },
    ).map { }.onStart { emit(Unit) }.map { listOfNotNull(nextEvent()) }.flowOn(Dispatchers.IO)

    private fun nextEvent(): SmartspaceTarget? {
        if (context.checkSelfPermission(Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            return null
        }
        val now = System.currentTimeMillis()
        val uri = CalendarContract.Instances.CONTENT_URI.buildUpon().also {
            ContentUris.appendId(it, now - 15 * 60 * 1000L)
            ContentUris.appendId(it, now + LOOKAHEAD_MILLIS)
        }.build()
        val cursor = try {
            context.contentResolver.query(
                uri,
                arrayOf(
                    CalendarContract.Instances.EVENT_ID,
                    CalendarContract.Instances.TITLE,
                    CalendarContract.Instances.BEGIN,
                    CalendarContract.Instances.ALL_DAY,
                ),
                "${CalendarContract.Instances.ALL_DAY} = 0 AND ${CalendarContract.Instances.VISIBLE} = 1",
                null,
                "${CalendarContract.Instances.BEGIN} ASC",
            )
        } catch (e: Exception) {
            null
        } ?: return null
        return cursor.use {
            if (!it.moveToFirst()) return null
            val id = it.getLong(0)
            val title = it.getString(1) ?: return null
            val begin = it.getLong(2)
            val time = DateFormat.getTimeFormat(context).format(Date(begin))
            SmartspaceTarget(
                id = "graphiteCalendarEvent$id",
                headerAction = SmartspaceAction(
                    id = "graphiteCalendarEventAction$id",
                    icon = Icon.createWithResource(context, R.drawable.ic_smartspace_event),
                    title = title,
                    subtitle = time,
                    intent = Intent(Intent.ACTION_VIEW)
                        .setData(ContentUris.withAppendedId(CalendarContract.Events.CONTENT_URI, id))
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                ),
                score = SmartspaceScores.SCORE_CALENDAR,
                featureType = SmartspaceTarget.FeatureType.FEATURE_CALENDAR,
            )
        }
    }

    companion object {
        private const val LOOKAHEAD_MILLIS = 4 * 60 * 60 * 1000L
    }
}
