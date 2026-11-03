package app.lawnchair.smartspace.provider

import android.service.notification.StatusBarNotification
import com.android.launcher3.notification.NotificationListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

/**
 * Graphite: the active notifications for At a Glance sources (timers, missed calls), read through
 * Launcher3's notification listener, which tells us about every change.
 */
object ActiveNotifications {
    private val changes = MutableStateFlow(0)

    @JvmStatic
    fun onChanged() {
        changes.update { it + 1 }
    }

    val notifications: Flow<List<StatusBarNotification>> = changes
        .map {
            runCatching { NotificationListener.getInstanceIfConnected()?.activeNotifications?.toList() }
                .getOrNull()
                .orEmpty()
        }
        .flowOn(Dispatchers.IO)
}
