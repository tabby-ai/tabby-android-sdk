package ai.tabby.android.ui.analytics

import ai.tabby.android.analytics.BaseInstallmentsEvent

internal data class SnippetCardRendered(
    override val installmentsCount: Int,
    override val countryName: String,
) : BaseInstallmentsEvent() {
    override val name: String = "snippet_card_rendered"
}