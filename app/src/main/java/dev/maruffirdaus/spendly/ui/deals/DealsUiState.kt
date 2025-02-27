package dev.maruffirdaus.spendly.ui.deals

import dev.maruffirdaus.spendly.common.constant.DealsSources
import dev.maruffirdaus.spendly.common.model.Deals

data class DealsUiState(
    val selectedTabIndex: Int = 0,
    val deals: Deals = Deals(),
    val dealGridPosition: Pair<Int, Int> = Pair(0, 0),
    val selectedDealsSource: DealsSources = DealsSources.entries[selectedTabIndex],
    val isLoading: Boolean = false,
    val isError: Boolean = false
)