package app.lawnchair.theme

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Handler
import android.os.Looper
import android.os.PatternMatcher
import androidx.core.graphics.ColorUtils
import app.lawnchair.preferences2.PreferenceManager2
import app.lawnchair.preferences2.firstCached
import app.lawnchair.theme.color.AndroidColor
import app.lawnchair.theme.color.ColorOption
import app.lawnchair.theme.color.ColorStyle
import app.lawnchair.theme.color.MonetColorSchemeCompat
import app.lawnchair.theme.color.SystemColorScheme
import app.lawnchair.ui.theme.getSystemAccent
import app.lawnchair.wallpaper.WallpaperManagerCompat
import com.android.launcher3.Utilities
import com.android.launcher3.dagger.ApplicationContext
import com.android.launcher3.dagger.LauncherAppComponent
import com.android.launcher3.dagger.LauncherAppSingleton
import com.android.launcher3.util.DaggerSingletonObject
import com.android.launcher3.util.DaggerSingletonTracker
import com.android.launcher3.util.SafeCloseable
import com.android.systemui.monet.Style
import com.patrykmichalik.opto.core.onEach
import dev.kdrag0n.colorkt.Color
import dev.kdrag0n.colorkt.conversion.ConversionGraph.convert
import dev.kdrag0n.colorkt.rgb.Srgb
import dev.kdrag0n.monet.theme.ColorScheme
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.CopyOnWriteArrayList
import javax.inject.Inject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel

@LauncherAppSingleton
class ThemeProvider @Inject constructor(
    @ApplicationContext private val context: Context,
    tracker: DaggerSingletonTracker,
) : SafeCloseable {
    private val preferenceManager2 = PreferenceManager2.getInstance(context)
    private val wallpaperManager = WallpaperManagerCompat.INSTANCE.get(context)
    private val coroutineScope = CoroutineScope(Dispatchers.Default)

    @Volatile
    private var accentColor: ColorOption = preferenceManager2.accentColor.firstCached()

    @Volatile
    private var colorStyle: ColorStyle = preferenceManager2.colorStyle.firstCached()

    private val colorSchemeMap = ConcurrentHashMap<Pair<Int, Style>, ColorScheme>()
    private val listeners = CopyOnWriteArrayList<ColorSchemeChangeListener>()
    private var overlayChangedReceiver: BroadcastReceiver? = null
    private val wallpaperColorsChangedListener =
        object : WallpaperManagerCompat.OnColorsChangedListener {
            override fun onColorsChanged() {
                if (accentColor is ColorOption.WallpaperPrimary) {
                    notifyColorSchemeChanged()
                }
                // The system palette follows the wallpaper shortly after
                notifySystemSchemeChangedLater()
            }
        }

    @Volatile
    private var isClosed = false

    init {
        if (Utilities.ATLEAST_S) {
            registerOverlayChangedListener()
        }
        wallpaperManager.addOnChangeListener(wallpaperColorsChangedListener)
        preferenceManager2.accentColor.onEach(launchIn = coroutineScope) {
            accentColor = it
            notifyColorSchemeChanged()
        }
        preferenceManager2.colorStyle.onEach(launchIn = coroutineScope) {
            colorStyle = it
            notifyColorSchemeChanged()
        }
        tracker.addCloseable(this)
    }

    private fun registerOverlayChangedListener() {
        val packageFilter = IntentFilter("android.intent.action.OVERLAY_CHANGED")
        packageFilter.addDataScheme("package")
        packageFilter.addDataSchemeSpecificPart("android", PatternMatcher.PATTERN_LITERAL)
        val receiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (accentColor is ColorOption.SystemAccent) {
                    notifyColorSchemeChanged()
                    notifySystemSchemeChangedLater()
                }
            }
        }
        context.registerReceiver(receiver, packageFilter, null, Handler(Looper.getMainLooper()))
        overlayChangedReceiver = receiver
    }

    val colorScheme get() = when (val accentColor = this.accentColor) {
        is ColorOption.SystemAccent -> systemColorScheme

        is ColorOption.WallpaperPrimary -> {
            val wallpaperPrimary = wallpaperManager.wallpaperColors?.primaryColor
            getColorScheme(wallpaperPrimary ?: ColorOption.LawnchairBlue.color, colorStyle.style)
        }

        is ColorOption.CustomColor -> getColorScheme(accentColor.color, colorStyle.style)

        else -> getColorScheme(ColorOption.LawnchairBlue.color, colorStyle.style)
    }

    // Graphite: the system scheme was cached under seed 0 and only refreshed for TONAL_SPOT on
    // OVERLAY_CHANGED, which can arrive before the app's resources have the new palette, so the
    // drawer kept the old Material You colours until a restart. Now the cache follows the system
    // accent colour as the resources report it, so a new wallpaper palette is always picked up.
    @Volatile private var systemScheme: ColorScheme? = null
    @Volatile private var systemSeed: Int? = null

    private val systemColorScheme get() = when {
        Utilities.ATLEAST_S -> {
            val seed = context.resources.getColor(android.R.color.system_accent1_500, null)
            if (colorStyle.style == Style.TONAL_SPOT) {
                systemScheme?.takeIf { systemSeed == seed }
                    ?: SystemColorScheme(context).also {
                        systemScheme = it
                        systemSeed = seed
                    }
            } else {
                getColorScheme(seed, colorStyle.style)
            }
        }
        else -> getColorScheme(context.getSystemAccent(darkTheme = false), colorStyle.style)
    }

    private val mainHandler = Handler(Looper.getMainLooper())

    /** Notify again once the resources have caught up with a new system palette */
    private fun notifySystemSchemeChangedLater() {
        if (accentColor !is ColorOption.SystemAccent) return
        listOf(500L, 2000L).forEach { delay ->
            mainHandler.postDelayed({ notifyColorSchemeChanged() }, delay)
        }
    }

    private fun getColorScheme(
        colorInt: Int,
        colorStyle: Style,
    ): ColorScheme {
        val key = Pair(colorInt, colorStyle)
        return colorSchemeMap.computeIfAbsent(key) {
            MonetColorSchemeCompat(colorInt, colorStyle)
        }
    }

    fun addListener(listener: ColorSchemeChangeListener) {
        listeners.add(listener)
    }

    fun removeListener(listener: ColorSchemeChangeListener) {
        listeners.remove(listener)
    }

    private fun notifyColorSchemeChanged() {
        if (isClosed) return
        listeners.forEach(ColorSchemeChangeListener::onColorSchemeChanged)
    }

    override fun close() {
        isClosed = true
        coroutineScope.cancel()
        wallpaperManager.removeOnChangeListener(wallpaperColorsChangedListener)
        overlayChangedReceiver?.let {
            context.unregisterReceiver(it)
            overlayChangedReceiver = null
        }
        listeners.clear()
    }

    companion object {
        @JvmField
        val INSTANCE = DaggerSingletonObject(LauncherAppComponent::getThemeProvider)
    }

    interface ColorSchemeChangeListener {
        fun onColorSchemeChanged()
    }
}

fun Color.toAndroidColor(): Int {
    return when (this) {
        is AndroidColor -> color
        is Srgb -> ColorUtils.setAlphaComponent(toRgb8(), 255)
        else -> convert<Srgb>().toAndroidColor()
    }
}
