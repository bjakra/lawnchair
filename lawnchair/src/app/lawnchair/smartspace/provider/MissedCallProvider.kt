package app.lawnchair.smartspace.provider

import android.app.Notification
import android.content.Context
import android.graphics.drawable.Icon
import android.service.notification.StatusBarNotification
import app.lawnchair.smartspace.model.SmartspaceAction
import app.lawnchair.smartspace.model.SmartspaceScores
import app.lawnchair.smartspace.model.SmartspaceTarget
import com.android.launcher3.R
import kotlinx.coroutines.flow.map

/** Graphite: the latest missed call, from the phone app's notification, until it's dismissed. */
class MissedCallProvider(context: Context) :
    SmartspaceDataSource(
        context,
        R.string.smartspace_missed_call,
        { smartspaceMissedCalls },
    ) {
    override val internalTargets = ActiveNotifications.notifications.map { list ->
        list.filter(::isMissedCall)
            .sortedWith(compareBy<StatusBarNotification> { it.notification.flags and Notification.FLAG_GROUP_SUMMARY == 0 }.thenBy { it.postTime })
            .lastOrNull()
            ?.let { listOf(target(it)) }
            .orEmpty()
    }

    // AOSP Dialer posts missed calls without a category, on its phone_missed_call channel.
    private fun isMissedCall(sbn: StatusBarNotification): Boolean {
        val n = sbn.notification
        return n.category == Notification.CATEGORY_MISSED_CALL ||
            n.channelId == "phone_missed_call" ||
            sbn.tag?.startsWith("MissedCall_") == true
    }

    private fun target(sbn: StatusBarNotification): SmartspaceTarget {
        val extras = sbn.notification.extras
        val caller = extras.getCharSequence(Notification.EXTRA_TEXT)
            ?: extras.getCharSequence(Notification.EXTRA_TITLE)
            ?: ""
        return SmartspaceTarget(
            id = "graphiteMissedCall",
            headerAction = SmartspaceAction(
                id = "graphiteMissedCallAction",
                icon = Icon.createWithResource(context, R.drawable.ic_smartspace_missed_call),
                title = context.getString(R.string.smartspace_missed_call),
                subtitle = caller,
                pendingIntent = sbn.notification.contentIntent,
            ),
            score = SmartspaceScores.SCORE_LOW_BATTERY,
            featureType = SmartspaceTarget.FeatureType.FEATURE_MISSED_CALL,
        )
    }
}
