package dev.maruffirdaus.spendly.ui.main

import dev.maruffirdaus.spendly.ui.main.constant.MainNavItems

sealed class MainEvent {
    data class OnNavItemClick(val navItem: MainNavItems) : MainEvent()

    data object OnMenuClick : MainEvent()
    data class OnMenuVisibilityChange(val visibility: Boolean) : MainEvent()

    data object OnSelectYearClick : MainEvent()
    data object OnDismissSelectYear : MainEvent()

    data object OnRefreshWallets : MainEvent()
}