package app.lawnchair.search.algorithms.engine.provider

import android.app.UiModeManager
import android.content.Context
import android.content.res.Configuration
import android.provider.Settings
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import app.lawnchair.preferences.PreferenceManager
import app.lawnchair.search.algorithms.engine.SearchProvider
import app.lawnchair.search.algorithms.engine.SearchResult
import com.android.launcher3.R
import java.text.Normalizer
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/** Graphite: quick settings as search results. Tapping one is handled by QuickToggleActivity. */
enum class QuickToggle(
    @StringRes val labelRes: Int,
    @DrawableRes val iconRes: Int,
    val keywords: List<String>,
) {
    WIFI(R.string.search_toggle_wifi, R.drawable.ic_search_toggle_wifi, listOf("wifi", "wlan", "internet")),
    BLUETOOTH(R.string.search_toggle_bluetooth, R.drawable.ic_search_toggle_bluetooth, listOf("bt")),
    AIRPLANE_MODE(R.string.search_toggle_airplane, R.drawable.ic_search_toggle_airplanemode_active, listOf("flight")),
    NIGHT_LIGHT(R.string.search_toggle_night_light, R.drawable.ic_search_toggle_filled_nightlight, emptyList()),
    DARK_THEME(R.string.search_toggle_dark_theme, R.drawable.ic_search_toggle_contrast, listOf("night mode")),
    AUTO_ROTATE(R.string.search_toggle_auto_rotate, R.drawable.ic_search_toggle_screen_rotation, listOf("rotation")),
    HOTSPOT(R.string.search_toggle_hotspot, R.drawable.ic_search_toggle_wifi_tethering, listOf("tether")),
    FLASHLIGHT(R.string.search_toggle_flashlight, R.drawable.ic_search_toggle_flashlight_on, listOf("torch")),
    CAST(R.string.search_toggle_cast, R.drawable.ic_search_toggle_cast, emptyList()),
    LOCATION(R.string.search_toggle_location, R.drawable.ic_search_toggle_location_on, listOf("gps")),
    ;

    /** Null when it has no readable on/off state, as those just open a settings page. */
    fun isActive(context: Context): Boolean? {
        val resolver = context.contentResolver
        return when (this) {
            WIFI -> Settings.Global.getInt(resolver, Settings.Global.WIFI_ON, 0) != 0
            BLUETOOTH -> Settings.Global.getInt(resolver, Settings.Global.BLUETOOTH_ON, 0) != 0
            AIRPLANE_MODE -> Settings.Global.getInt(resolver, Settings.Global.AIRPLANE_MODE_ON, 0) != 0
            NIGHT_LIGHT -> Settings.Secure.getInt(resolver, NIGHT_DISPLAY_ACTIVATED, 0) != 0
            AUTO_ROTATE -> Settings.System.getInt(resolver, Settings.System.ACCELEROMETER_ROTATION, 0) != 0
            LOCATION -> Settings.Secure.getInt(resolver, Settings.Secure.LOCATION_MODE, 0) != 0
            DARK_THEME -> {
                val mode = context.getSystemService(UiModeManager::class.java).nightMode
                if (mode == UiModeManager.MODE_NIGHT_AUTO) {
                    context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK ==
                        Configuration.UI_MODE_NIGHT_YES
                } else {
                    mode == UiModeManager.MODE_NIGHT_YES
                }
            }
            HOTSPOT, FLASHLIGHT, CAST -> null
        }
    }

    companion object {
        const val NIGHT_DISPLAY_ACTIVATED = "night_display_activated"
    }
}

object QuickToggleSearchProvider : SearchProvider {
    private const val MIN_QUERY_LENGTH = 2
    private const val MAX_RESULTS = 4
    private val DIACRITICS = Regex("\\p{Mn}+")

    override val id: String = "quick_toggle"

    override fun search(context: Context, query: String): Flow<List<SearchResult>> = flow {
        val needle = query.trim().normalized()
        if (needle.length < MIN_QUERY_LENGTH ||
            !PreferenceManager.getInstance(context).searchResultQuickToggles.get()
        ) {
            emit(emptyList())
            return@flow
        }
        emit(
            QuickToggle.entries.asSequence()
                .filter { toggle ->
                    context.getString(toggle.labelRes).normalized().contains(needle) ||
                        toggle.keywords.any { it.startsWith(needle) }
                }
                .take(MAX_RESULTS)
                .map { SearchResult.QuickToggleResult(it, it.isActive(context)) }
                .toList(),
        )
    }

    private fun String.normalized() =
        Normalizer.normalize(lowercase(), Normalizer.Form.NFD).replace(DIACRITICS, "")
}
