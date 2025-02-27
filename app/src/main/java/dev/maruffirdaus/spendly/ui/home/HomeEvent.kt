package dev.maruffirdaus.spendly.ui.home

import dev.maruffirdaus.spendly.common.model.Wallet

sealed class HomeEvent {
    data class OnScrollPositionChange(val scrollPosition: Int) : HomeEvent()

    data class OnDeleteWalletClick(val wallet: Wallet) : HomeEvent()
    data class OnConfirmDeleteWallet(val onSuccess: (() -> Unit)? = null) : HomeEvent()

    data object OnCancelDeleteWallet : HomeEvent()

    data class OnRefreshGraphData(val walletId: String, val year: String) : HomeEvent()
}