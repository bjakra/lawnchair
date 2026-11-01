package app.lawnchair.icons

import android.content.Context
import android.util.Log
import app.lawnchair.icons.shape.IconShape
import app.lawnchair.icons.shape.PathShapeDelegate
import app.lawnchair.preferences.PreferenceChangeListener
import app.lawnchair.preferences.PreferenceManager
import app.lawnchair.preferences2.PreferenceManager2
import app.lawnchair.preferences2.firstCached
import app.lawnchair.theme.ThemeProvider
import app.lawnchair.theme.color.tokens.ColorTokens
import com.android.launcher3.LauncherPrefs
import com.android.launcher3.R
import com.android.launcher3.Utilities
import com.android.launcher3.graphics.ShapeDelegate
import com.android.launcher3.graphics.ThemeManager.Companion.ICON_FACTORY_DAGGER_KEY
import com.android.launcher3.graphics.theme.IconThemeFactory
import com.android.launcher3.graphics.theme.ThemePreference
import com.android.launcher3.concurrent.annotations.Ui
import com.android.launcher3.dagger.ApplicationContext
import com.android.launcher3.dagger.LauncherAppSingleton
import com.android.launcher3.graphics.ThemeManager
import com.android.launcher3.icons.mono.MonoIconThemeController
import com.android.launcher3.icons.mono.ColorList
import com.android.launcher3.util.DaggerSingletonTracker
import com.android.launcher3.display.DisplayController
import com.android.launcher3.display.LauncherDisplayInfo
import com.android.launcher3.display.OverlayChangeHandler
import com.android.launcher3.util.LooperExecutor
import javax.inject.Inject
import javax.inject.Named
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.merge
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.plus

@LauncherAppSingleton
class LawnchairThemeManager
@Inject
constructor(
    @ApplicationContext private val context: Context,
    @Ui private val uiExecutor: LooperExecutor,
    prefs: LauncherPrefs,
    private val themePreference: ThemePreference,
    @Named(ICON_FACTORY_DAGGER_KEY)
    iconThemeFactories: Map<String, @JvmSuppressWildcards IconThemeFactory>,
    overlayChangeHandler: OverlayChangeHandler,
    private val lifecycle: DaggerSingletonTracker,
    private val prefs2: PreferenceManager2,
    private val prefs1: PreferenceManager,
    private val themeProvider: ThemeProvider,
) : ThemeManager(
    context,
    prefs,
    themePreference,
    iconThemeFactories,
    uiExecutor,
    overlayChangeHandler,
    lifecycle,
) {
    private val statePrefs1 = listOf(
        prefs1.wrapAdaptiveIcons,
        prefs1.transparentIconBackground,
        prefs1.shadowBGIcons,
        prefs1.coloredBackgroundLightness,
        prefs1.forceIconMonochrome,
        prefs1.launcherTheme,
    )

    private val prefListener = PreferenceChangeListener {
        uiExecutor.execute { verifyIconState() }
    }

    override var iconState = parseIconStateV2(null)

    init {
        val scope = MainScope() + CoroutineName("LawnchairThemeManager")
        merge(
            prefs2.iconShape.get(),
            prefs2.customIconShape.get(),
            prefs2.folderShape.get(),
            prefs2.customFolderShape.get(),
        ).onEach { verifyIconState() }
            .launchIn(scope)

        val colorSchemeChangeListener = object : ThemeProvider.ColorSchemeChangeListener {
            @Volatile
            private var isDestroyed = false

            override fun onColorSchemeChanged() {
                synchronized(this) {
                    if (isDestroyed) return
                    uiExecutor.execute {
                        synchronized(this) {
                            if (isDestroyed) return@execute
                            verifyIconState()
                        }
                    }
                }
            }

            fun destroy() {
                synchronized(this) {
                    isDestroyed = true
                    themeProvider.removeListener(this)
                }
            }
        }
        themeProvider.addListener(colorSchemeChangeListener)

        val displayInfoCloseable = DisplayController.INSTANCE.get(context).listenable?.forEachChange(uiExecutor) { _, flags ->
            if (flags and LauncherDisplayInfo.CHANGE_NIGHT_MODE != 0) {
                verifyIconState()
            }
        }

        statePrefs1.forEach { it.addListener(prefListener) }

        lifecycle.addCloseable {
            colorSchemeChangeListener.destroy()
            scope.cancel()
            displayInfoCloseable?.close()
            statePrefs1.forEach { it.removeListener(prefListener) }
        }
    }

    override fun verifyIconState() {
        val newState = parseIconStateV2(iconState)
        val oldState = iconState
        if (newState == oldState) return
        iconState = newState

        listeners.forEach { it.onThemeChanged() }
        if (newState.iconShape != oldState.iconShape) {
            _iconShapeData.dispatchValue(iconShape.createIconShape(iconShapeData.value.pathSize))
        }
    }

    private fun prefs1State(): String = statePrefs1.joinToString(",") { it.get().toString() }

    private fun parseIconStateV2(oldState: IconState?): IconState {
        val currentAppShape: IconShape = try {
            prefs2.iconShape.firstCached()
        } catch (e: Exception) {
            Log.d(TAG, "Error getting icon shape", e)
            IconShape.Circle
        }

        val currentFolderShape: IconShape = try {
            prefs2.folderShape.firstCached()
        } catch (e: Exception) {
            Log.d(TAG, "Error getting folder shape", e)
            IconShape.Circle
        }

        val currentPrefs1State = prefs1State()
        val appShapeKey = currentAppShape.getHashString() + currentPrefs1State
        val folderShapeKey = currentFolderShape.getHashString() + currentPrefs1State
        val themeController = themePreference.value?.let {
            if (prefs1.forceIconMonochrome.get()) {
                FORCED_MONO_THEME_CONTROLLER
            } else {
                MONO_THEME_CONTROLLER
            }
        }
        val themedColorsKey =
            if (themeController != null) {
                lawnchairThemedColors(context).toString()
            } else {
                ""
            }
        val combinedKey = "$appShapeKey:$folderShapeKey:$themedColorsKey"

        val appShape =
            if (oldState != null && (oldState.iconShape as? PathShapeDelegate)?.iconShape == currentAppShape) {
                oldState.iconShape
            } else {
                PathShapeDelegate(currentAppShape)
            }

        val folderShape =
            if (oldState != null && (oldState.folderShape as? PathShapeDelegate)?.iconShape == currentFolderShape) {
                oldState.folderShape
            } else {
                PathShapeDelegate(currentFolderShape)
            }

        return IconState(
            iconMask = combinedKey,
            folderRadius = 1f,
            shapeRadius = 1f,
            themeController = themeController,
            themeCode = themeController?.themeID ?: "no-theme",
            iconShape = appShape,
            folderShape = folderShape,
            fileShape = oldState?.fileShape ?: ShapeDelegate.GenericPathShape(
                context.resources.getString(R.string.home_screen_files_file_icon_path_data),
            ),
        )
    }
}

// Reuse controllers to allow the equality check in verifyIconState.
private val MONO_THEME_CONTROLLER = MonoIconThemeController(colorProvider = ::lawnchairThemedColors)
private val FORCED_MONO_THEME_CONTROLLER =
    MonoIconThemeController(shouldForceThemeIcon = true, colorProvider = ::lawnchairThemedColors)

/** Lawnchair's themed icon colours, from its own colour tokens. */
internal fun lawnchairThemedColors(context: Context): ColorList {
    val (background, foreground) = if (Utilities.isDarkTheme(context)) {
        ColorTokens.Accent2_800.resolveColor(context) to ColorTokens.Accent1_200.resolveColor(context)
    } else {
        ColorTokens.Accent1_100.resolveColor(context) to ColorTokens.Accent1_700.resolveColor(context)
    }
    return ColorList(
        iconBackgroundColor = background,
        iconForegroundColor = foreground,
        iconAdaptiveBackgroundColor = background,
        badgeBackgroundColor = background,
        badgeForegroundColor = foreground,
    )
}
private const val TAG = "LawnchairThemeManager"
