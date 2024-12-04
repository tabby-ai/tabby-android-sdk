package ai.tabby.android.internal.ui.analutics

import ai.tabby.android.analytics.BaseInstallmentsEvent

internal data class LearnMorePopupClosed(
    override val installmentsCount: Int,
    override val countryName: String,
) : BaseInstallmentsEvent() {
    override val name: String = "learn_more_popup_closed"
}