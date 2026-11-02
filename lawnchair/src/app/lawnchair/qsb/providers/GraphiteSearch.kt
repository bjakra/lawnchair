package app.lawnchair.qsb.providers

import android.app.ActivityOptions
import android.content.Intent
import app.lawnchair.qsb.ThemingMethod
import com.android.launcher3.Launcher
import com.android.launcher3.R

/** Graphite's search app (PixelishSearch fork), preinstalled as a system app. */
data object GraphiteSearch : QsbSearchProvider(
    id = "graphite_search",
    name = R.string.search_provider_graphite,
    icon = R.drawable.ic_qsb_search,
    themingMethod = ThemingMethod.TINT,
    packageName = "org.graphite.search",
    className = "com.pchmn.pixelishsearch.search.MainActivity",
    action = Intent.ACTION_MAIN,
    website = "https://duckduckgo.com/",
) {
    // No window animation, so the search UI feels like part of the launcher
    override suspend fun launch(launcher: Launcher, forceWebsite: Boolean) {
        if (forceWebsite) {
            super.launch(launcher, forceWebsite)
            return
        }
        val options = ActivityOptions.makeCustomAnimation(launcher, 0, 0).toBundle()
        launcher.startActivity(createSearchIntent(), options)
    }
}
