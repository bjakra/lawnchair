package app.lawnchair.smartspace.provider

import android.app.AlarmManager
import android.content.Context
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.text.format.DateFormat
import app.lawnchair.smartspace.model.SmartspaceAction
import app.lawnchair.smartspace.model.SmartspaceScores
import app.lawnchair.smartspace.model.SmartspaceTarget
import app.lawnchair.util.broadcastReceiverFlow
import com.android.launcher3.R
import java.util.Date
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/** Graphite: the next alarm when it is less than 12 hours away, as on the Pixel's At a Glance. */
class NextAlarmProvider(context: Context) :
    SmartspaceDataSource(
        context,
        R.string.smartspace_next_alarm,
        { smartspaceNextAlarm },
    ) {
    private val alarmManager = context.getSystemService(AlarmManager::class.java)

    override val internalTargets = broadcastReceiverFlow(
        context,
        IntentFilter(AlarmManager.ACTION_NEXT_ALARM_CLOCK_CHANGED).apply {
            addAction(android.content.Intent.ACTION_TIME_TICK)
        },
    ).map { }.onStart { emit(Unit) }.map { listOfNotNull(target()) }

    private fun target(): SmartspaceTarget? {
        val next = alarmManager?.nextAlarmClock ?: return null
        val inMillis = next.triggerTime - System.currentTimeMillis()
        if (inMillis < 0 || inMillis > 12 * 60 * 60 * 1000L) return null
        val time = DateFormat.getTimeFormat(context).format(Date(next.triggerTime))
        return SmartspaceTarget(
            id = "graphiteNextAlarm",
            headerAction = SmartspaceAction(
                id = "graphiteNextAlarmAction",
                icon = Icon.createWithResource(context, R.drawable.ic_smartspace_alarm),
                title = context.getString(R.string.smartspace_next_alarm_title),
                subtitle = time,
                pendingIntent = next.showIntent,
            ),
            score = SmartspaceScores.SCORE_CALENDAR,
            featureType = SmartspaceTarget.FeatureType.FEATURE_ALARM,
        )
    }
}
