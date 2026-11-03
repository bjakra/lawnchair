package app.lawnchair.animation

import android.content.Context
import android.view.animation.Interpolator
import android.view.animation.PathInterpolator
import app.lawnchair.util.getPrefsIfUnlocked
import kotlin.math.min
import kotlin.math.roundToLong

/**
 * App open and close animation settings from Lawnchair's recents page: a speed factor for the
 * timed transitions and the swipe home springs, and a style that changes the curves.
 */
object AnimationTuning {
    const val STYLE_STANDARD = "standard"
    const val STYLE_SMOOTH = "smooth"
    const val STYLE_BOUNCY = "bouncy"

    private const val MIN_SPEED = 50
    private const val MAX_SPEED = 200

    // Long, soft deceleration that settles without a visible stop, like iOS and HyperOS.
    private val SMOOTH_OPEN = PathInterpolator(0.32f, 0.72f, 0f, 1f)

    @JvmStatic
    fun speed(context: Context): Float {
        val percent = getPrefsIfUnlocked(context)?.appAnimationSpeed?.get() ?: 100
        return percent.coerceIn(MIN_SPEED, MAX_SPEED) / 100f
    }

    @JvmStatic
    fun style(context: Context): String = getPrefsIfUnlocked(context)?.appAnimationStyle?.get() ?: STYLE_STANDARD

    @JvmStatic
    fun duration(context: Context, base: Long): Long = (base / speed(context)).roundToLong()

    @JvmStatic
    fun duration(context: Context, base: Int): Int = duration(context, base.toLong()).toInt()

    /** A spring's period goes with 1/sqrt(stiffness), so the speed factor applies squared. */
    @JvmStatic
    fun stiffness(context: Context, base: Float): Float {
        val speed = speed(context)
        return when (style(context)) {
            STYLE_SMOOTH -> base * 0.8f * speed * speed
            else -> base * speed * speed
        }
    }

    @JvmStatic
    fun damping(context: Context, base: Float): Float = when (style(context)) {
        STYLE_SMOOTH -> 1f
        STYLE_BOUNCY -> min(base, 0.72f)
        else -> base
    }

    @JvmStatic
    fun openInterpolator(context: Context, base: Interpolator): Interpolator =
        if (style(context) == STYLE_SMOOTH) SMOOTH_OPEN else base
}
