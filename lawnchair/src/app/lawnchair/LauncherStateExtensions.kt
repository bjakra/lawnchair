package app.lawnchair

import com.android.launcher3.Launcher
import com.android.launcher3.LauncherState
import com.android.launcher3.anim.AnimatorListeners.forEndCallback
import kotlin.coroutines.resume
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Opens the app drawer with the launcher's own state transition. Graphite: the previous hand-built
 * animation drew the drawer at its end position for a frame before animating from the home screen,
 * which showed as a flash when tapping the search bar.
 */
suspend fun Launcher.animateToAllApps() {
    if (isInState(LauncherState.ALL_APPS)) return
    suspendCancellableCoroutine { cont ->
        stateManager.goToState(
            LauncherState.ALL_APPS,
            true,
            forEndCallback(Runnable { if (cont.isActive) cont.resume(Unit) }),
        )
    }
}
