package app.lawnchair.qsb

import android.content.Context
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import com.android.launcher3.R

/**
 * Graphite: Material You colours for the search bars on the home screen and in the app drawer, like
 * the Pixel launcher's bar. [LAWNCHAIR] keeps Lawnchair's own colours and the "accent colour" switch.
 */
enum class QsbColorStyle(
    @param:StringRes val labelRes: Int,
    @param:ColorRes private val backgroundRes: Int,
    @param:ColorRes private val foregroundRes: Int,
) {
    NEUTRAL(R.string.qsb_color_neutral, R.color.materialColorSurfaceContainerHigh, R.color.materialColorOnSurfaceVariant),
    PRIMARY(R.string.qsb_color_primary, R.color.materialColorPrimaryContainer, R.color.materialColorOnPrimaryContainer),
    SECONDARY(R.string.qsb_color_secondary, R.color.materialColorSecondaryContainer, R.color.materialColorOnSecondaryContainer),
    TERTIARY(R.string.qsb_color_tertiary, R.color.materialColorTertiaryContainer, R.color.materialColorOnTertiaryContainer),
    LAWNCHAIR(R.string.qsb_color_lawnchair, 0, 0),
    ;

    val isMaterialYou get() = this != LAWNCHAIR

    fun background(context: Context): Int = context.getColor(backgroundRes)

    fun foreground(context: Context): Int = context.getColor(foregroundRes)

    companion object {
        fun fromString(value: String) = entries.firstOrNull { it.name.equals(value, ignoreCase = true) } ?: NEUTRAL
    }
}
