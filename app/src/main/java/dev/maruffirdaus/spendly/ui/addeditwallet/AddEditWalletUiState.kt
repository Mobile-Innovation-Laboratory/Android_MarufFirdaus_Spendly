package dev.maruffirdaus.spendly.ui.addeditwallet

data class AddEditWalletUiState(
    val title: String = "",
    val currency: String = "",
    val balance: String = "",
    val balanceError: String? = null,
    val isLoading: Boolean = false
)