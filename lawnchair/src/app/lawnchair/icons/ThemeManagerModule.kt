package app.lawnchair.icons

import android.content.Context
import app.lawnchair.preferences.PreferenceManager
import app.lawnchair.preferences2.PreferenceManager2
import app.lawnchair.theme.ThemeProvider
import com.android.launcher3.LauncherPrefs
import com.android.launcher3.concurrent.annotations.Ui
import com.android.launcher3.dagger.ApplicationContext
import com.android.launcher3.dagger.LauncherAppSingleton
import com.android.launcher3.display.OverlayChangeHandler
import com.android.launcher3.graphics.ThemeManager
import com.android.launcher3.graphics.ThemeManager.Companion.ICON_FACTORY_DAGGER_KEY
import com.android.launcher3.graphics.theme.IconThemeFactory
import com.android.launcher3.graphics.theme.ThemePreference
import com.android.launcher3.util.DaggerSingletonTracker
import com.android.launcher3.util.LooperExecutor
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
class ThemeManagerModule {

    @Provides
    @LauncherAppSingleton
    fun provideThemeManager(
        @ApplicationContext context: Context,
        @Ui uiExecutor: LooperExecutor,
        prefs: LauncherPrefs,
        themePreference: ThemePreference,
        @Named(ICON_FACTORY_DAGGER_KEY)
        iconThemeFactories: Map<String, @JvmSuppressWildcards IconThemeFactory>,
        overlayChangeHandler: OverlayChangeHandler,
        lifecycle: DaggerSingletonTracker,
        prefs2: PreferenceManager2,
        prefs1: PreferenceManager,
        themeProvider: ThemeProvider,
    ): ThemeManager {
        return LawnchairThemeManager(
            context = context,
            uiExecutor = uiExecutor,
            prefs = prefs,
            themePreference = themePreference,
            iconThemeFactories = iconThemeFactories,
            overlayChangeHandler = overlayChangeHandler,
            lifecycle = lifecycle,
            prefs2 = prefs2,
            prefs1 = prefs1,
            themeProvider = themeProvider,
        )
    }
}
