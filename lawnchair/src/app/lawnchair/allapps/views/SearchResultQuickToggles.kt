package app.lawnchair.allapps.views

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import app.lawnchair.search.QuickToggleActivity
import app.lawnchair.search.adapter.SearchTargetCompat
import app.lawnchair.search.algorithms.engine.provider.QuickToggle
import com.android.launcher3.R

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
        applyState(tile, state == STATE_ON)
        tile.setOnClickListener {
            context.startActivity(
                Intent(context, QuickToggleActivity::class.java)
                    .putExtra(QuickToggleActivity.EXTRA_TOGGLE, toggle.name),
            )
            if (state != STATE_UNKNOWN && toggle in DIRECT_TOGGLES) {
                state = if (state == STATE_ON) STATE_OFF else STATE_ON
                applyState(tile, state == STATE_ON)
            }
        }
    }

    private fun applyState(tile: View, on: Boolean) {
        val background = context.getColor(
            if (on) R.color.materialColorPrimary else R.color.materialColorSurfaceContainerHigh,
        )
        val foreground = context.getColor(
            if (on) R.color.materialColorOnPrimary else R.color.materialColorOnSurface,
        )
        tile.background = GradientDrawable().apply {
            cornerRadius = resources.displayMetrics.density * TILE_RADIUS_DP
            setColor(background)
        }
        tile.findViewById<ImageView>(R.id.quick_toggle_icon).imageTintList = ColorStateList.valueOf(foreground)
        tile.findViewById<TextView>(R.id.quick_toggle_label).setTextColor(foreground)
    }

    companion object {
        const val EXTRA_TOGGLES = "quick_toggles"
        const val EXTRA_STATES = "quick_toggle_states"
        const val STATE_UNKNOWN = -1
        const val STATE_OFF = 0
        const val STATE_ON = 1
        private const val TILE_RADIUS_DP = 36f

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
