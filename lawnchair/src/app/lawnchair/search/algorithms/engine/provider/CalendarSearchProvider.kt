package app.lawnchair.search.algorithms.engine.provider

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.provider.CalendarContract
import android.util.Log
import app.lawnchair.preferences.PreferenceManager
import app.lawnchair.search.algorithms.engine.SearchProvider
import app.lawnchair.search.algorithms.engine.SearchResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

/** Graphite: searches the titles of upcoming events; a recurring event shows its next occurrence. */
object CalendarSearchProvider : SearchProvider {
    private const val TAG = "CalendarSearchProvider"
    private const val MAX_RESULTS = 3
    private const val MAX_ROWS_SCANNED = 500
    private const val WINDOW_MILLIS = 365L * 24 * 60 * 60 * 1000

    override val id: String = "calendar"

    override fun search(context: Context, query: String): Flow<List<SearchResult>> = flow {
        val enabled = PreferenceManager.getInstance(context).searchResultCalendar.get()
        val granted = context.checkSelfPermission(Manifest.permission.READ_CALENDAR) ==
            PackageManager.PERMISSION_GRANTED
        emit(if (query.isBlank() || !enabled || !granted) emptyList() else findEvents(context, query))
    }.flowOn(Dispatchers.IO)

    private fun findEvents(context: Context, query: String): List<SearchResult> {
        val now = System.currentTimeMillis()
        val uri = CalendarContract.Instances.CONTENT_URI.buildUpon()
            .appendPath(now.toString())
            .appendPath((now + WINDOW_MILLIS).toString())
            .build()
        val projection = arrayOf(
            CalendarContract.Instances.EVENT_ID,
            CalendarContract.Instances.TITLE,
            CalendarContract.Instances.BEGIN,
            CalendarContract.Instances.END,
            CalendarContract.Instances.ALL_DAY,
        )
        val selection = "${CalendarContract.Instances.TITLE} LIKE ? AND " +
            "${CalendarContract.Calendars.VISIBLE} = 1"
        val results = mutableListOf<SearchResult>()
        try {
            context.contentResolver.query(
                uri, projection, selection, arrayOf("%$query%"),
                "${CalendarContract.Instances.BEGIN} ASC",
            )?.use { cursor ->
                val seen = mutableSetOf<Long>()
                var scanned = 0
                while (cursor.moveToNext() && results.size < MAX_RESULTS) {
                    if (scanned++ >= MAX_ROWS_SCANNED) break
                    val eventId = cursor.getLong(0)
                    if (!seen.add(eventId)) continue
                    results += SearchResult.CalendarEvent(
                        id = eventId,
                        title = cursor.getString(1) ?: continue,
                        begin = cursor.getLong(2),
                        end = cursor.getLong(3),
                        allDay = cursor.getInt(4) == 1,
                    )
                }
            }
        } catch (e: RuntimeException) {
            Log.w(TAG, "Calendar query failed", e)
        }
        return results
    }
}
