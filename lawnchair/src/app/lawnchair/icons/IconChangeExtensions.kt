package app.lawnchair.icons

import android.os.UserHandle
import androidx.annotation.WorkerThread
import app.lawnchair.LawnchairApp
import com.android.launcher3.LauncherModel
import com.android.launcher3.model.tasks.PackageUpdatedTask
import com.android.launcher3.model.tasks.ShortcutsChangedTask
import com.android.launcher3.shortcuts.ShortcutRequest

/** Refreshes the icons of a package outside of package events, e.g. after a custom icon change. */
@WorkerThread
fun LauncherModel.onAppIconChanged(packageName: String, user: UserHandle) {
    enqueueModelUpdateTask(PackageUpdatedTask(PackageUpdatedTask.OP_UPDATE, user, packageName))
    ShortcutRequest(LawnchairApp.instance, user)
        .forPackage(packageName)
        .query(ShortcutRequest.PINNED)
        .let {
            if (it.isNotEmpty()) {
                enqueueModelUpdateTask(ShortcutsChangedTask(packageName, it, user, false))
            }
        }
}
