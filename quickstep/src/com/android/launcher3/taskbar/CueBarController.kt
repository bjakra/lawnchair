package com.android.launcher3.taskbar

import android.graphics.Rect
import android.graphics.Region
import java.io.PrintWriter

/**
 * Graphite: AOSP's Ambient Cue bar needs Google's personal context (ACE) libraries, so it is
 * removed. This no-op keeps the taskbar code that calls it identical to AOSP.
 */
class CueBarController(activity: TaskbarActivityContext) :
    TaskbarControllers.LoggableTaskbarController {
    val isExpanded: Boolean = false
    val isVisible: Boolean = false

    fun init(controllers: TaskbarControllers) {}

    fun cleanUpOverlay() {}

    fun onDestroy() {}

    override fun dumpLogs(prefix: String?, pw: PrintWriter?) {}

    fun onTaskbarStatusUpdated(visible: Boolean, stashed: Boolean) {}

    fun onRecentsButtonLayoutChanged(bounds: Rect) {}

    fun updateStateForSysuiFlags(systemUiStateFlags: Long) {}

    fun addTouchableRegion(region: Region) {}
}
