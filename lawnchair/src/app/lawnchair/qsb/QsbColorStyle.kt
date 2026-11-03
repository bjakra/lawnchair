package app.lawnchair.qsb

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import app.lawnchair.preferences.observeAsState
import app.lawnchair.preferences.preferenceManager
import app.lawnchair.preferences2.asState
import app.lawnchair.preferences2.preferenceManager2
import com.android.launcher3.R

/**
 * Graphite: Material You colours for the search bars on the home screen and in the app drawer, like
 * the Pixel launcher's bar. [LAWNCHAIR] keeps Lawnchair's own colours and the "accent colour" switch.
 * [AUTO], the default, matches the home screen: strong Material You while themed icons are on,
 * Lawnchair's colours otherwise. Resolve it with [rememberQsbColorStyle] before drawing.
 */
enum class QsbColorStyle(
    @param:StringRes val labelRes: Int,
    @param:ColorRes private val backgroundRes: Int,
    @param:ColorRes private val foregroundRes: Int,
) {
    AUTO(R.string.qsb_color_auto, 0, 0),
    NEUTRAL(R.string.qsb_color_neutral, R.color.materialColorSurfaceContainerHigh, R.color.materialColorOnSurfaceVariant),
    PRIMARY(R.string.qsb_color_primary, R.color.materialColorPrimaryContainer, R.color.materialColorOnPrimaryContainer),
    SECONDARY(R.string.qsb_color_secondary, R.color.materialColorSecondaryContainer, R.color.materialColorOnSecondaryContainer),
    TERTIARY(R.string.qsb_color_tertiary, R.color.materialColorTertiaryContainer, R.color.materialColorOnTertiaryContainer),
    LAWNCHAIR(R.string.qsb_color_lawnchair, 0, 0),
    ;

    val isMaterialYou get() = this != LAWNCHAIR && this != AUTO

    fun resolve(themedIcons: Boolean) = if (this == AUTO) {
        if (themedIcons) PRIMARY else LAWNCHAIR
    } else {
        this
    }

    fun background(context: Context): Int = context.getColor(backgroundRes)

    fun foreground(context: Context): Int = context.getColor(foregroundRes)

    companion object {
        fun fromString(value: String) = entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: AUTO
    }
}

/** The search bar colour style to draw, with [QsbColorStyle.AUTO] resolved against themed icons. */
@Composable
fun rememberQsbColorStyle(): QsbColorStyle {
    val prefs = preferenceManager()
    val style = preferenceManager2().qsbColorStyle.asState().value
    val themedIcons = prefs.themedIcons.observeAsState().value || prefs.drawerThemedIcons.observeAsState().value
    return style.resolve(themedIcons)
}
