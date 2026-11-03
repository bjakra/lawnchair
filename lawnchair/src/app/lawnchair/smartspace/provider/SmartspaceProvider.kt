package app.lawnchair.smartspace.provider

import android.app.Activity
import android.content.Context
import app.lawnchair.smartspace.model.SmartspaceAction
import app.lawnchair.smartspace.model.SmartspaceTarget
import app.lawnchair.ui.preferences.PreferenceActivity
import app.lawnchair.ui.preferences.navigation.Smartspace
import app.lawnchair.util.dropWhileBusy
import com.android.launcher3.R
import com.android.launcher3.dagger.ApplicationContext
import com.android.launcher3.dagger.LauncherAppComponent
import com.android.launcher3.dagger.LauncherAppSingleton
import com.android.launcher3.util.DaggerSingletonObject
import com.android.launcher3.util.SafeCloseable
import javax.inject.Inject
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.shareIn

@LauncherAppSingleton
class SmartspaceProvider @Inject constructor(
    @ApplicationContext private val context: Context,
) : SafeCloseable {

    val dataSources = listOf(
        SmartspaceWidgetReader(context),
        GraphiteWeatherProvider(context),
        CalendarEventProvider(context),
        NextAlarmProvider(context),
        RecognisedSongProvider(context),
        ClockTimerProvider(context),
        MissedCallProvider(context),
        DoNotDisturbProvider(context),
        BluetoothBatteryProvider(context),
        BatteryStatusProvider(context),
        TorchProvider(context),
        NowPlayingProvider(context),
        OnboardingProvider(context),
    )

    private val state = dataSources
        .map { it.targets }
        .reduce { acc, flow -> flow.combine(acc) { a, b -> a + b } }
        .shareIn(
            MainScope(),
            SharingStarted.WhileSubscribed(),
            replay = 1,
        )
    val targets = state
        .map {
            val targets = singleDateCard(it.targets)
            if (it.requiresSetup.isNotEmpty()) {
                listOf(setupTarget) + targets
            } else {
                targets
            }
        }
    val previewTargets = state
        .map { singleDateCard(it.targets) }

    /**
     * Graphite: weather targets are drawn as the date card. Keep one: the first with weather data,
     * else a single plain date card, so there's no second page with just the date again.
     */
    private fun singleDateCard(targets: List<SmartspaceTarget>): List<SmartspaceTarget> {
        val dateCards = targets.filter { it.featureType == SmartspaceTarget.FeatureType.FEATURE_WEATHER }
        if (dateCards.size <= 1) return targets
        val keep = dateCards.firstOrNull { it.headerAction?.subtitle?.isNotEmpty() == true } ?: dateCards.first()
        return targets.filter { it.featureType != SmartspaceTarget.FeatureType.FEATURE_WEATHER || it === keep }
    }

    private val setupTarget = SmartspaceTarget(
        id = "smartspaceSetup",
        headerAction = SmartspaceAction(
            id = "smartspaceSetupAction",
            title = context.getString(R.string.smartspace_requires_setup),
            intent = PreferenceActivity.createIntent(context, Smartspace),
        ),
        score = 999f,
        featureType = SmartspaceTarget.FeatureType.FEATURE_TIPS,
    )

    suspend fun startSetup(activity: Activity) {
        state
            .map { it.requiresSetup }
            .dropWhileBusy()
            .collect { sources ->
                sources.forEach {
                    it.startSetup(activity)
                    it.onSetupDone()
                }
            }
    }

    override fun close() {
        TODO("Not yet implemented")
    }

    companion object {
        @JvmField val INSTANCE = DaggerSingletonObject(LauncherAppComponent::getSmartspaceProvider)
    }
}
