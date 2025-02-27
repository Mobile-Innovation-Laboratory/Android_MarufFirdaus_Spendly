package dev.maruffirdaus.spendly.ui.deals

import dev.maruffirdaus.spendly.common.constant.DealsSources

sealed class DealsEvent {
    data class OnSelectedTabIndexChange(val selectedTabIndex: Int) : DealsEvent()

    data class OnDealGridPositionChange(val index: Int, val offset: Int) : DealsEvent()

    data class OnRefreshDeals(
        val source: DealsSources,
        val country: String,
        val offset: Int? = null
    ) : DealsEvent()
}