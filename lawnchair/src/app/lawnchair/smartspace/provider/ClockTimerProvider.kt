package app.lawnchair.smartspace.provider

import android.app.Notification
import android.content.Context
import android.graphics.drawable.Icon
import android.os.SystemClock
import android.service.notification.StatusBarNotification
import android.text.format.DateFormat
import android.view.View
import android.view.ViewGroup
import android.widget.Chronometer
import android.widget.FrameLayout
import app.lawnchair.smartspace.model.SmartspaceAction
import app.lawnchair.smartspace.model.SmartspaceScores
import app.lawnchair.smartspace.model.SmartspaceTarget
import com.android.launcher3.R
import java.util.Date
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/** Graphite: a running timer (with the time it ends) or stopwatch from the clock app. */
class ClockTimerProvider(context: Context) :
    SmartspaceDataSource(
        context,
        R.string.smartspace_timer,
        { smartspaceTimer },
    ) {
    override val internalTargets = ActiveNotifications.notifications.map { list ->
        list.filter(::isClockChronometer)
            .sortedByDescending { it.postTime }
            .take(1)
            .map { withContext(Dispatchers.Main) { target(it) } }
    }

    private fun isClockChronometer(sbn: StatusBarNotification): Boolean {
        val n = sbn.notification
        if (n.flags and Notification.FLAG_GROUP_SUMMARY != 0) return false
        if (sbn.packageName in CLOCK_PACKAGES) {
            // AOSP DeskClock draws its timers and stopwatch in a custom view with a Chronometer.
            val channel = n.channelId.orEmpty().lowercase()
            return sbn.isOngoing && ("timer" in channel || "stopwatch" in channel)
        }
        return n.extras.getBoolean(Notification.EXTRA_SHOW_CHRONOMETER) && n.category == Notification.CATEGORY_STOPWATCH
    }

    /** Count down flag and end time (wall clock), from the extras or the custom view's Chronometer. */
    private fun chronometer(n: Notification): Pair<Boolean, Long>? {
        if (n.extras.getBoolean(Notification.EXTRA_SHOW_CHRONOMETER)) {
            return n.extras.getBoolean(Notification.EXTRA_CHRONOMETER_COUNT_DOWN) to n.`when`
        }
        val views = n.contentView ?: n.bigContentView ?: return null
        val chronometer = runCatching { findChronometer(views.apply(context, FrameLayout(context))) }.getOrNull()
            ?: return null
        return chronometer.isCountDown to System.currentTimeMillis() + (chronometer.base - SystemClock.elapsedRealtime())
    }

    private fun findChronometer(view: View): Chronometer? {
        if (view is Chronometer) return view
        if (view is ViewGroup) {
            for (i in 0 until view.childCount) findChronometer(view.getChildAt(i))?.let { return it }
        }
        return null
    }

    private fun target(sbn: StatusBarNotification): SmartspaceTarget {
        val n = sbn.notification
        val chronometer = chronometer(n)
        val countDown = chronometer?.first ?: !n.channelId.orEmpty().contains("stopwatch", ignoreCase = true)
        val title = if (countDown) R.string.smartspace_timer_title else R.string.smartspace_stopwatch_title
        val subtitle = if (countDown && chronometer != null) {
            context.getString(R.string.smartspace_timer_ends, DateFormat.getTimeFormat(context).format(Date(chronometer.second)))
        } else {
            context.getString(R.string.smartspace_stopwatch_running)
        }
        return SmartspaceTarget(
            id = "graphiteTimer${sbn.key}",
            headerAction = SmartspaceAction(
                id = "graphiteTimerAction${sbn.key}",
                icon = Icon.createWithResource(context, R.drawable.ic_smartspace_timer),
                title = context.getString(title),
                subtitle = subtitle,
                pendingIntent = n.contentIntent,
            ),
            score = SmartspaceScores.SCORE_LOW_BATTERY,
            featureType = SmartspaceTarget.FeatureType.FEATURE_TIMER,
        )
    }

    companion object {
        private val CLOCK_PACKAGES = setOf("com.android.deskclock", "com.google.android.deskclock")
    }
}
