package app.lawnchair.allapps.views

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.PathInterpolator
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.lawnchair.search.QuickToggleActivity
import app.lawnchair.search.adapter.SearchTargetCompat
import app.lawnchair.search.algorithms.engine.provider.QuickToggle
import com.android.launcher3.R
import com.android.launcher3.util.MSDLPlayerWrapper
import com.google.android.msdl.data.model.MSDLToken

/**
 * Graphite: a row of up to two quick setting tiles, styled like the Pixel launcher's search: filled
 * with the primary colour when on. The tile updates right away, since search results aren't re-read
 * after a tap.
 */
class SearchResultQuickToggles(context: Context, attrs: AttributeSet?) :
    LinearLayout(context, attrs),
    SearchResultView {

    override val isQuickLaunch = false

    override fun launch() = false

    override fun bind(target: SearchTargetCompat, shortcuts: List<SearchTargetCompat>) {
        val names = target.extras.getStringArray(EXTRA_TOGGLES).orEmpty()
        val states = target.extras.getIntArray(EXTRA_STATES) ?: IntArray(names.size)
        listOf(R.id.quick_toggle_first, R.id.quick_toggle_second).forEachIndexed { index, id ->
            val tile = findViewById<View>(id)
            val toggle = names.getOrNull(index)?.let { name -> QuickToggle.entries.find { it.name == name } }
            tile.visibility = if (toggle == null) INVISIBLE else VISIBLE
            if (toggle != null) bindTile(tile, toggle, states[index])
        }
    }

    private fun bindTile(tile: View, toggle: QuickToggle, initialState: Int) {
        tile.findViewById<ImageView>(R.id.quick_toggle_icon).setImageResource(toggle.iconRes)
        tile.findViewById<TextView>(R.id.quick_toggle_label).setText(toggle.labelRes)
        var state = initialState
        applyState(tile, state == STATE_ON, animate = false)
        tile.setOnTouchListener { view, event ->
            // SystemUI's tiles shrink slightly while pressed and spring back on release
            when (event.actionMasked) {
                MotionEvent.ACTION_DOWN -> view.animate().scaleX(PRESSED_SCALE).scaleY(PRESSED_SCALE)
                    .setDuration(PRESS_MILLIS).setInterpolator(DecelerateInterpolator()).start()
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> view.animate().scaleX(1f).scaleY(1f)
                    .setDuration(RELEASE_MILLIS).setInterpolator(OvershootInterpolator(2f)).start()
            }
            false
        }
        tile.setOnClickListener {
            context.startActivity(
                Intent(context, QuickToggleActivity::class.java)
                    .putExtra(QuickToggleActivity.EXTRA_TOGGLE, toggle.name),
            )
            if (state != STATE_UNKNOWN && toggle in DIRECT_TOGGLES) {
                state = if (state == STATE_ON) STATE_OFF else STATE_ON
                applyState(tile, state == STATE_ON, animate = true)
            }
            // The same haptics as SystemUI's quick settings tiles
            MSDLPlayerWrapper.INSTANCE.get(context).playToken(
                when (state) {
                    STATE_ON -> MSDLToken.SWITCH_ON
                    STATE_OFF -> MSDLToken.SWITCH_OFF
                    else -> MSDLToken.TAP_HIGH_EMPHASIS
                },
            )
        }
    }

    /**
     * Graphite: like SystemUI's tiles, an inactive tile is a pill and an active one has 24dp corners;
     * the shape and colours animate between the two.
     */
    private fun applyState(tile: View, on: Boolean, animate: Boolean) {
        val density = resources.displayMetrics.density
        val background = context.getColor(
            if (on) R.color.materialColorPrimary else R.color.materialColorSurfaceContainerHigh,
        )
        val foreground = context.getColor(
            if (on) R.color.materialColorOnPrimary else R.color.materialColorOnSurface,
        )
        val radius = density * (if (on) ACTIVE_RADIUS_DP else INACTIVE_RADIUS_DP)
        val icon = tile.findViewById<ImageView>(R.id.quick_toggle_icon)
        val label = tile.findViewById<TextView>(R.id.quick_toggle_label)
        val drawable = (tile.background as? GradientDrawable) ?: GradientDrawable().also { tile.background = it }
        (tile.getTag(R.id.quick_toggle_icon) as? ValueAnimator)?.cancel()
        if (!animate) {
            drawable.cornerRadius = radius
            drawable.setColor(background)
            icon.imageTintList = ColorStateList.valueOf(foreground)
            label.setTextColor(foreground)
            tile.setTag(R.id.quick_toggle_label, Triple(radius, background, foreground))
            return
        }
        @Suppress("UNCHECKED_CAST")
        val (fromRadius, fromBackground, fromForeground) =
            tile.getTag(R.id.quick_toggle_label) as? Triple<Float, Int, Int> ?: Triple(radius, background, foreground)
        val evaluator = ArgbEvaluator()
        val animator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = STATE_MILLIS
            interpolator = PathInterpolator(0.2f, 0f, 0f, 1f)
            addUpdateListener {
                val t = it.animatedFraction
                drawable.cornerRadius = fromRadius + (radius - fromRadius) * t
                drawable.setColor(evaluator.evaluate(t, fromBackground, background) as Int)
                val fg = evaluator.evaluate(t, fromForeground, foreground) as Int
                icon.imageTintList = ColorStateList.valueOf(fg)
                label.setTextColor(fg)
            }
        }
        tile.setTag(R.id.quick_toggle_icon, animator)
        tile.setTag(R.id.quick_toggle_label, Triple(radius, background, foreground))
        animator.start()
    }

    companion object {
        const val EXTRA_TOGGLES = "quick_toggles"
        const val EXTRA_STATES = "quick_toggle_states"
        const val STATE_UNKNOWN = -1
        const val STATE_OFF = 0
        const val STATE_ON = 1
        private const val ACTIVE_RADIUS_DP = 24f
        private const val INACTIVE_RADIUS_DP = 50f
        private const val PRESSED_SCALE = 0.94f
        private const val PRESS_MILLIS = 100L
        private const val RELEASE_MILLIS = 350L
        private const val STATE_MILLIS = 350L

        /** The rest open a settings page, so their state doesn't change on tap. */
        private val DIRECT_TOGGLES = setOf(
            QuickToggle.AIRPLANE_MODE,
            QuickToggle.NIGHT_LIGHT,
            QuickToggle.DARK_THEME,
            QuickToggle.AUTO_ROTATE,
            QuickToggle.LOCATION,
        )
    }
}
