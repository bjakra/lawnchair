package app.lawnchair.search.algorithms.engine.provider.apps

import android.app.usage.UsageStatsManager
import android.content.Context
import android.os.SystemClock
import android.util.Log
import com.android.launcher3.model.data.AppInfo
import kotlin.math.exp

/**
 * Graphite: orders app matches by how much and how recently they are used, using Android's own usage
 * statistics, which stay on the device. A launch counts half as much every 14 days.
 */
object AppUsageRanking {
    private const val TAG = "AppUsageRanking"
    private const val WINDOW_DAYS = 56L
    private const val HALF_LIFE_DAYS = 14.0
    private const val DAY_MILLIS = 24L * 60 * 60 * 1000
    private const val CACHE_MILLIS = 60_000L

    @Volatile
    private var scores: Map<String, Double> = emptyMap()

    @Volatile
    private var loadedAt = 0L

    fun rank(context: Context, apps: List<AppInfo>, query: String): List<AppInfo> {
        val current = scores(context)
        return apps.sortedWith(
            compareByDescending<AppInfo> { it.title?.toString()?.startsWith(query, ignoreCase = true) == true }
                .thenByDescending { current[it.componentName?.packageName] ?: 0.0 },
        )
    }

    private fun scores(context: Context): Map<String, Double> {
        val time = SystemClock.elapsedRealtime()
        if (loadedAt != 0L && time - loadedAt < CACHE_MILLIS) return scores
        val now = System.currentTimeMillis()
        scores = try {
            context.getSystemService(UsageStatsManager::class.java)
                .queryAndAggregateUsageStats(now - WINDOW_DAYS * DAY_MILLIS, now)
                .mapValues { (_, stats) ->
                    val days = (now - stats.lastTimeUsed).coerceAtLeast(0) / DAY_MILLIS.toDouble()
                    stats.mAppLaunchCount * exp(-days * Math.log(2.0) / HALF_LIFE_DAYS)
                }
        } catch (e: SecurityException) {
            Log.w(TAG, "No access to usage statistics", e)
            emptyMap()
        }
        loadedAt = time
        return scores
    }
}
