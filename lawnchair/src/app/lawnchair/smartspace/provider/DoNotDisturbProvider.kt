package app.lawnchair.smartspace.provider

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.drawable.Icon
import android.provider.Settings
import app.lawnchair.smartspace.model.SmartspaceAction
import app.lawnchair.smartspace.model.SmartspaceScores
import app.lawnchair.smartspace.model.SmartspaceTarget
import app.lawnchair.util.broadcastReceiverFlow
import com.android.launcher3.R
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/** Graphite: shows when Do Not Disturb or another mode silences notifications. */
class DoNotDisturbProvider(context: Context) :
    SmartspaceDataSource(
        context,
        R.string.smartspace_dnd,
        { smartspaceDoNotDisturb },
    ) {
    private val notificationManager = context.getSystemService(NotificationManager::class.java)

    override val internalTargets = broadcastReceiverFlow(
        context,
        IntentFilter(NotificationManager.ACTION_INTERRUPTION_FILTER_CHANGED),
    ).map { }.onStart { emit(Unit) }.map { listOfNotNull(target()) }

    private fun target(): SmartspaceTarget? {
        val filter = notificationManager?.currentInterruptionFilter ?: return null
        if (filter == NotificationManager.INTERRUPTION_FILTER_ALL ||
            filter == NotificationManager.INTERRUPTION_FILTER_UNKNOWN
        ) {
            return null
        }
        return SmartspaceTarget(
            id = "graphiteDoNotDisturb",
            headerAction = SmartspaceAction(
                id = "graphiteDoNotDisturbAction",
                icon = Icon.createWithResource(context, R.drawable.ic_smartspace_dnd),
                title = context.getString(R.string.smartspace_dnd),
                subtitle = context.getString(R.string.smartspace_dnd_on),
                intent = Intent(Settings.ACTION_ZEN_MODE_PRIORITY_SETTINGS)
                    .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
            ),
            score = SmartspaceScores.SCORE_BATTERY,
            featureType = SmartspaceTarget.FeatureType.FEATURE_CALENDAR,
        )
    }
}
