package dev.maruffirdaus.spendly.ui.welcome

import dev.maruffirdaus.spendly.common.model.User

data class WelcomeUiState(
    val user: User? = null,
    val title: String = "",
    val currency: String = "",
    val balance: String = "",
    val balanceError: String? = null,
    val isDataSyncEnabled: Boolean = false,
    val isLoading: Boolean = false,
    val isEnableDataSyncDialogVisible: Boolean = false
)