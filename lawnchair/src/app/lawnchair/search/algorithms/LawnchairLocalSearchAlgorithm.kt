package app.lawnchair.search.algorithms

import android.content.Context
import app.lawnchair.preferences.PreferenceManager
import app.lawnchair.preferences2.PreferenceManager2
import app.lawnchair.preferences2.firstCached
import app.lawnchair.search.adapter.SearchLinksTarget
import app.lawnchair.search.adapter.SearchTargetCompat
import app.lawnchair.search.adapter.SearchTargetFactory
import app.lawnchair.search.algorithms.engine.ActionsSectionBuilder
import app.lawnchair.search.algorithms.engine.AppsAndShortcutsSectionBuilder
import app.lawnchair.search.algorithms.engine.CalculationSectionBuilder
import app.lawnchair.search.algorithms.engine.CalendarSectionBuilder
import app.lawnchair.search.algorithms.engine.QuickToggleSectionBuilder
import app.lawnchair.search.algorithms.engine.ContactsSectionBuilder
import app.lawnchair.search.algorithms.engine.EmptyStateSectionBuilder
import app.lawnchair.search.algorithms.engine.FilesSectionBuilder
import app.lawnchair.search.algorithms.engine.HistorySectionBuilder
import app.lawnchair.search.algorithms.engine.SearchProvider
import app.lawnchair.search.algorithms.engine.SearchResult
import app.lawnchair.search.algorithms.engine.SearchSettingsSectionBuilder
import app.lawnchair.search.algorithms.engine.SectionBuilder
import app.lawnchair.search.algorithms.engine.SettingsSectionBuilder
import app.lawnchair.search.algorithms.engine.WebSuggestionsSectionBuilder
import app.lawnchair.search.algorithms.engine.provider.CalculatorSearchProvider
import app.lawnchair.search.algorithms.engine.provider.CalendarSearchProvider
import app.lawnchair.search.algorithms.engine.provider.QuickToggleSearchProvider
import app.lawnchair.search.algorithms.engine.provider.ContactsSearchProvider
import app.lawnchair.search.algorithms.engine.provider.FileSearchProvider
import app.lawnchair.search.algorithms.engine.provider.HistorySearchProvider
import app.lawnchair.search.algorithms.engine.provider.SettingsSearchProvider
import app.lawnchair.search.algorithms.engine.provider.ShortcutSearchProvider
import app.lawnchair.search.algorithms.engine.provider.apps.AppSearchProvider
import app.lawnchair.search.algorithms.engine.provider.web.CustomWebSearchProvider
import app.lawnchair.search.algorithms.engine.provider.web.WebSuggestionProvider
import com.android.launcher3.LauncherAppState
import com.android.launcher3.R
import com.android.launcher3.allapps.BaseAllAppsAdapter
import com.android.launcher3.search.SearchCallback
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LawnchairLocalSearchAlgorithm(context: Context) : LawnchairSearchAlgorithm(context) {

    private val appState = LauncherAppState.getInstance(context)

    private val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private var currentJob: Job? = null
    private var modelRetryJob: Job? = null

    private val appSearchProvider = AppSearchProvider
    private val shortcutSearchProvider = ShortcutSearchProvider
    private val historySearchProvider = HistorySearchProvider

    private val searchProviders: List<SearchProvider> = listOf(
        QuickToggleSearchProvider,
        CalendarSearchProvider,
        SettingsSearchProvider,
        FileSearchProvider,
        ContactsSearchProvider,
        WebSuggestionProvider,
    )

    override fun doSearch(query: String, callback: SearchCallback<BaseAllAppsAdapter.AdapterItem>) {
        modelRetryJob?.cancel()
        modelRetryJob = appState.model.runWhenModelLoaded(coroutineScope) { apps ->
            // Graphite: the "Apps and shortcuts" switch wasn't read at all. The Settings app's own
            // shortcuts (Wi-Fi, Battery, ...) count as Android settings results, so that switch hides them too.
            val legacyPrefs = PreferenceManager.getInstance(context)
            val appsEnabled = legacyPrefs.searchResultApps.get()
            val appResults = if (appsEnabled) appSearchProvider.search(context, query, apps) else emptyList()
            val shortcutResults = shortcutSearchProvider.search(context, appResults).filter {
                legacyPrefs.searchResultSettingsEntry.get() || !it.isSettingsAppShortcut()
            }

            currentJob?.cancel()
            currentJob = coroutineScope.launch {
                // Each provider flow starts by emitting an empty list so that combine() can
                // produce a first value without waiting for the slowest provider (e.g. web
                // suggestions blocked on a network roundtrip, or contacts/files with no
                // timeout). App and shortcut matches are computed synchronously above, so this
                // lets them appear immediately and then be augmented as providers complete.
                // See issue #7325 (symptom 1: results gated behind the slowest provider).
                val nonAppProvidersFlow = combine(
                    searchProviders.map { provider ->
                        provider.search(context, query).map { it to true }
                            .onStart { emit(emptyList<SearchResult>() to false) }
                    },
                ) { resultsArray ->
                    resultsArray.all { it.second } to resultsArray.flatMap { it.first }
                }

                // Graphite: results used to be shown as soon as any provider answered, so rows such as
                // "Search on Play Store" appeared and were then pushed down by slower sections. Wait until
                // every provider has answered, or a short settle time has passed, then show them together.
                var latest: List<SearchResult> = emptyList()
                var shown = false
                suspend fun show(nonAppResults: List<SearchResult>) {
                    val calcResult = CalculatorSearchProvider.search(context, query)
                        .firstOrNull()

                    val allResults = appResults + shortcutResults + (calcResult ?: emptyList()) + nonAppResults + generateActionResults(query)

                    val searchTargets = translateToSearchTargets(allResults)
                    setFirstItemQuickLaunch(searchTargets)
                    val adapterItems = transformSearchResults(searchTargets)
                    withContext(Dispatchers.Main) {
                        callback.onSearchResult(query, ArrayList(adapterItems))
                    }
                }
                val settle = launch {
                    delay(SETTLE_MILLIS)
                    if (!shown) {
                        shown = true
                        show(latest)
                    }
                }
                nonAppProvidersFlow.collect { (allAnswered, nonAppResults) ->
                    latest = nonAppResults
                    if (allAnswered || shown) {
                        settle.cancel()
                        shown = true
                        show(nonAppResults)
                    }
                }
            }
        }
    }

    /**
     * Graphite: before anything is typed, show the most used apps instead of the whole app list, as
     * on the Pixel launcher, followed by recent searches when those are enabled.
     */
    override fun doZeroStateSearch(callback: SearchCallback<BaseAllAppsAdapter.AdapterItem>) {
        modelRetryJob?.cancel()
        currentJob?.cancel()

        val prefs = PreferenceManager.getInstance(context)
        val historyEnabled = prefs.searchResulRecentSuggestion.get()

        modelRetryJob = appState.model.runWhenModelLoaded(coroutineScope) { apps ->
            currentJob = coroutineScope.launch {
                val topApps = appSearchProvider.topApps(context, apps, ZERO_STATE_APP_COUNT)
                val historyResults = if (historyEnabled) {
                    val maxHistory = PreferenceManager2.getInstance(context).maxRecentResultCount.firstCached()
                    historySearchProvider.getRecentKeywords(context, maxHistory)
                } else {
                    emptyList()
                }
                val searchTargets = translateToSearchTargets(
                    topApps + historyResults + listOf(SearchResult.Action.SearchSettings),
                )
                val adapterItems = transformSearchResults(searchTargets)
                withContext(Dispatchers.Main) {
                    callback.onSearchResult("", ArrayList(adapterItems))
                }
            }
        }
    }

    override fun cancel(interruptActiveRequests: Boolean) {
        modelRetryJob?.cancel()
        currentJob?.cancel()
    }

    private fun generateActionResults(query: String): List<SearchResult.Action> {
        val actions = mutableListOf<SearchResult.Action>()
        val prefs = PreferenceManager.getInstance(context)
        val prefs2 = PreferenceManager2.getInstance(context)

        if (prefs.searchResultStartPageSuggestion.get()) {
            val provider = prefs2.webSuggestionProvider.firstCached()
            val webProvider = provider.configure(context)

            val providerName = if (webProvider is CustomWebSearchProvider) {
                webProvider.getDisplayName()
            } else {
                context.getString(webProvider.label)
            }

            actions.add(
                SearchResult.Action.WebSearch(
                    query = query,
                    providerName = providerName,
                    searchUrl = webProvider.getSearchUrl(query),
                    providerIconRes = webProvider.iconRes,
                    tintIcon = webProvider is CustomWebSearchProvider,
                ),
            )
        }

        if (SearchLinksTarget.resolveMarketSearchActivity(context) != null) {
            actions.add(SearchResult.Action.MarketSearch(query = query))
        }

        actions.add(SearchResult.Action.SearchSettings)

        return actions
    }

    private val sectionBuilders: List<SectionBuilder> = listOf(
        AppsAndShortcutsSectionBuilder,
        CalculationSectionBuilder,
        QuickToggleSectionBuilder,
        CalendarSectionBuilder,
        WebSuggestionsSectionBuilder,
        ContactsSectionBuilder,
        FilesSectionBuilder,
        SettingsSectionBuilder,
        HistorySectionBuilder,
        ActionsSectionBuilder,
        EmptyStateSectionBuilder,
        SearchSettingsSectionBuilder,
    )

    private fun translateToSearchTargets(
        results: List<SearchResult>,
    ): List<SearchTargetCompat> {
        val factory = SearchTargetFactory(context)

        // The new function is just a flatMap. It's declarative and beautiful.
        return sectionBuilders.flatMap { builder ->
            builder.build(context, factory, results)
        }
    }
}

private const val ZERO_STATE_APP_COUNT = 4

/** How long a search waits for slow providers before showing what it has. */
private const val SETTLE_MILLIS = 250L

/** Graphite: a launcher shortcut published by the Settings app, such as Wi-Fi or Battery. */
private fun SearchResult.isSettingsAppShortcut(): Boolean =
    this is SearchResult.Shortcut && data.`package` == "com.android.settings"
