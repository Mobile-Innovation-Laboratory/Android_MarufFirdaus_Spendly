package dev.maruffirdaus.spendly.ui.common

import dev.maruffirdaus.spendly.common.model.Wallet

sealed class SharedEvent {
    data class OnSelectedWalletChange(val wallet: Wallet) : SharedEvent()

    data class OnSelectedYearChange(val year: String) : SharedEvent()

    data class OnSnackbarMessageChange(val message: String?) : SharedEvent()

    data object OnRefreshSelectedWallet : SharedEvent()
}