package app.lawnchair.smartspace.model

import android.content.Context
import android.os.Build
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import app.lawnchair.util.isPackageInstalledAndEnabled
import com.android.launcher3.R

sealed class SmartspaceMode(
    @StringRes val nameResourceId: Int,
    @LayoutRes val layoutResourceId: Int,
) {
    companion object {
        fun fromString(value: String): SmartspaceMode = when (value) {
            "smartspacer" -> Smartspacer
            else -> LawnchairSmartspace
        }

        /**
         * @return The list of all smartspace options
         */
        fun values() = listOf(
            LawnchairSmartspace,
            Smartspacer,
        )
    }

    abstract fun isAvailable(context: Context): Boolean
}

object LawnchairSmartspace : SmartspaceMode(
    nameResourceId = R.string.smartspace_mode_lawnchair,
    layoutResourceId = R.layout.smartspace_container,
) {
    override fun toString() = "lawnchair"
    override fun isAvailable(context: Context): Boolean = true
}

object Smartspacer : SmartspaceMode(
    nameResourceId = R.string.smartspace_mode_smartspacer,
    layoutResourceId = R.layout.smartspace_smartspacer,
) {
    override fun toString(): String = "smartspacer"

    override fun isAvailable(context: Context): Boolean {
        // Smartspacer requires Android 10+
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q &&
            context.packageManager.isPackageInstalledAndEnabled("com.kieronquinn.app.smartspacer")
    }
}
