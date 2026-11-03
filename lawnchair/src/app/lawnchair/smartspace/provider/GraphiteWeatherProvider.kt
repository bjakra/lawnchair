package app.lawnchair.smartspace.provider

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.database.ContentObserver
import android.graphics.drawable.Icon
import android.net.Uri
import android.os.Handler
import android.os.Looper
import app.lawnchair.smartspace.model.SmartspaceAction
import app.lawnchair.smartspace.model.SmartspaceScores
import app.lawnchair.smartspace.model.SmartspaceTarget
import com.android.launcher3.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

/**
 * Graphite: the current weather from Graphite Weather, shown beside the date as on the Pixel's
 * At a Glance. Nothing is shown while weather is off in Graphite settings.
 */
class GraphiteWeatherProvider(context: Context) :
    SmartspaceDataSource(
        context,
        R.string.smartspace_weather,
        { smartspaceWeather },
    ) {

    override val internalTargets = callbackFlow {
        val observer = object : ContentObserver(Handler(Looper.getMainLooper())) {
            override fun onChange(selfChange: Boolean) {
                trySend(Unit)
            }
        }
        try {
            context.contentResolver.registerContentObserver(WEATHER_URI, false, observer)
        } catch (e: SecurityException) {
            // Graphite Weather isn't installed; there is simply no weather then
        }
        trySend(Unit)
        awaitClose { context.contentResolver.unregisterContentObserver(observer) }
    }.map { listOfNotNull(readWeather()) }.flowOn(Dispatchers.IO)

    private fun readWeather(): SmartspaceTarget? {
        val cursor = try {
            context.contentResolver.query(WEATHER_URI, null, null, null, null)
        } catch (e: Exception) {
            null
        } ?: return null
        return cursor.use {
            if (!it.moveToFirst()) return null
            val state = it.getInt(it.getColumnIndexOrThrow("state"))
            val temperature = it.getInt(it.getColumnIndexOrThrow("temperature"))
            val description = it.getString(it.getColumnIndexOrThrow("description"))
            SmartspaceTarget(
                id = "graphiteWeather",
                headerAction = SmartspaceAction(
                    id = "graphiteWeatherAction",
                    icon = Icon.createWithResource(context, iconFor(state)),
                    title = "",
                    subtitle = "$temperature°",
                    contentDescription = "$description, $temperature°",
                    intent = Intent().setComponent(SETTINGS_COMPONENT)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK),
                ),
                score = SmartspaceScores.SCORE_WEATHER,
                featureType = SmartspaceTarget.FeatureType.FEATURE_WEATHER,
            )
        }
    }

    /** Graphite Weather reports SystemUI's WeatherStateIcon values. */
    private fun iconFor(state: Int) = when (state) {
        1 -> R.drawable.ic_graphite_weather_sun
        2, 4 -> R.drawable.ic_graphite_weather_moon
        3, 5, 6 -> R.drawable.ic_graphite_weather_partly
        10 -> R.drawable.ic_graphite_weather_fog
        18 -> R.drawable.ic_graphite_weather_storm
        21, 22 -> R.drawable.ic_graphite_weather_snow
        11, 12, 13, 14, 15 -> R.drawable.ic_graphite_weather_rain
        else -> R.drawable.ic_graphite_weather_cloud
    }

    companion object {
        private val WEATHER_URI = Uri.parse("content://org.graphite.weather.data/current")
        private val SETTINGS_COMPONENT =
            ComponentName("org.graphite.weather", "org.graphite.weather.WeatherSettingsActivity")
    }
}
