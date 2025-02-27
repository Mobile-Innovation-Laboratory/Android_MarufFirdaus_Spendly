package dev.maruffirdaus.spendly.ui.welcome

sealed class WelcomeEvent {
    data class OnTitleChange(val title: String) : WelcomeEvent()

    data class OnCurrencyChange(val currency: String) : WelcomeEvent()

    data class OnBalanceChange(val balance: String) : WelcomeEvent()
    data class OnBalanceError(val error: String) : WelcomeEvent()

    data object OnShowEnableDataSyncDialog : WelcomeEvent()
    data object OnHideEnableDataSyncDialog : WelcomeEvent()

    data class OnSaveWallet(val onSuccess: (() -> Unit)? = null) : WelcomeEvent()

    data object OnRefreshUser : WelcomeEvent()

    data object OnRefreshDataSyncEnabled : WelcomeEvent()
}