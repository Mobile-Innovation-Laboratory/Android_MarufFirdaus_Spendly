package dev.maruffirdaus.spendly.ui.addeditwallet

sealed class AddEditWalletEvent {
    data class OnSaveWallet(val walletId: String? = null, val onSuccess: (() -> Unit)? = null) :
        AddEditWalletEvent()

    data class OnTitleChange(val title: String) : AddEditWalletEvent()

    data class OnCurrencyChange(val currency: String) : AddEditWalletEvent()

    data class OnBalanceChange(val balance: String) : AddEditWalletEvent()
    data class OnBalanceError(val error: String) : AddEditWalletEvent()

    data class OnRefreshWallet(val walletId: String) : AddEditWalletEvent()
}