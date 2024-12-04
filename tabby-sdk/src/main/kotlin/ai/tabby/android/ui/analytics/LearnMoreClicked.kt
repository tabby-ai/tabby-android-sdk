package ai.tabby.android.ui.analytics

import ai.tabby.android.analytics.BaseInstallmentsEvent

internal data class LearnMoreClicked(
    override val installmentsCount: Int,
    override val countryName: String,
) : BaseInstallmentsEvent() {
    override val name: String = "learn_more_clicked"
}