package app.lawnchair.search.algorithms.engine.provider

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import java.text.Normalizer

/**
 * Graphite: finds real Settings screens by their visible name, by asking Android which activities
 * the Settings app exports with a label of their own. The list is built once per process.
 */
object SettingsPageIndex {
    data class Page(val label: String, val component: ComponentName)

    private const val SETTINGS_PACKAGE = "com.android.settings"
    private const val MIN_QUERY_LENGTH = 2
    private val DIACRITICS = Regex("\\p{Mn}+")

    @Volatile
    private var pages: List<Page>? = null

    fun search(context: Context, query: String, max: Int): List<Page> {
        val needle = query.trim().normalized()
        if (needle.length < MIN_QUERY_LENGTH) return emptyList()
        val all = pages ?: discover(context).also { pages = it }
        val (starting, containing) = all
            .filter { it.label.normalized().contains(needle) }
            .partition { it.label.normalized().startsWith(needle) }
        return (starting + containing).take(max)
    }

    private fun discover(context: Context): List<Page> {
        val pm = context.packageManager
        val appLabel = runCatching {
            pm.getApplicationLabel(pm.getApplicationInfo(SETTINGS_PACKAGE, 0)).toString().trim()
        }.getOrDefault("")
        return pm.queryIntentActivities(Intent(Intent.ACTION_MAIN).setPackage(SETTINGS_PACKAGE), 0)
            .asSequence()
            .map { it.activityInfo }
            .filter { it.exported && it.labelRes != 0 }
            .mapNotNull { info ->
                val label = info.loadLabel(pm).toString().trim()
                if (label.isEmpty() || label == appLabel) null else Page(label, ComponentName(info.packageName, info.name))
            }
            .distinctBy { it.label.lowercase() }
            .sortedBy { it.label.lowercase() }
            .toList()
    }

    private fun String.normalized() =
        Normalizer.normalize(lowercase(), Normalizer.Form.NFD).replace(DIACRITICS, "")
}
