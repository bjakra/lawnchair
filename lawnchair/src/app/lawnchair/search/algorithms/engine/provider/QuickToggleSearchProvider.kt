package app.lawnchair.search.algorithms.engine.provider

import android.app.UiModeManager
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.res.Configuration
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.net.wifi.WifiManager
import android.os.Handler
import android.os.Looper
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

    /** On or off like SystemUI's tile; null only if the state can't be read. */
    fun isActive(context: Context): Boolean? {
        val resolver = context.contentResolver
        return when (this) {
            WIFI -> Settings.Global.getInt(resolver, Settings.Global.WIFI_ON, 0) != 0
            // Turning on counts as on, like SystemUI's tile
            BLUETOOTH -> runCatching {
                val state = context.getSystemService(BluetoothManager::class.java)!!.adapter!!.state
                state == BluetoothAdapter.STATE_ON || state == BluetoothAdapter.STATE_TURNING_ON
            }.getOrElse { Settings.Global.getInt(resolver, Settings.Global.BLUETOOTH_ON, 0) != 0 }
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
            // Hidden WifiManager API; Lawnchair is platform signed, so it may call it
            HOTSPOT -> runCatching {
                WifiManager::class.java.getMethod("isWifiApEnabled")
                    .invoke(context.getSystemService(WifiManager::class.java)) as Boolean
            }.getOrDefault(false)
            FLASHLIGHT -> TorchState.isOn(context)
            // Only opens the cast settings; off like SystemUI's tile while nothing is cast
            CAST -> false
        }
    }

    companion object {
        const val NIGHT_DISPLAY_ACTIVATED = "night_display_activated"
    }
}

/** Graphite: the torch state, which CameraManager only reports through a callback. */
object TorchState {
    @Volatile private var enabled = false
    private var registered = false

    fun isOn(context: Context): Boolean {
        register(context.applicationContext)
        return enabled
    }

    @Synchronized
    private fun register(context: Context) {
        if (registered) return
        registered = true
        runCatching {
            val camera = context.getSystemService(CameraManager::class.java)
            val id = camera.cameraIdList.firstOrNull {
                camera.getCameraCharacteristics(it).get(CameraCharacteristics.FLASH_INFO_AVAILABLE) == true
            } ?: return
            camera.registerTorchCallback(
                object : CameraManager.TorchCallback() {
                    override fun onTorchModeChanged(cameraId: String, on: Boolean) {
                        if (cameraId == id) enabled = on
                    }
                },
                Handler(Looper.getMainLooper()),
            )
        }
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
