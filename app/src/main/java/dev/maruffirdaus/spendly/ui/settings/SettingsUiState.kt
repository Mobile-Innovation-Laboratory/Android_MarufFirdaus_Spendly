package dev.maruffirdaus.spendly.ui.settings

import dev.maruffirdaus.spendly.common.model.User

data class SettingsUiState(
    val user: User? = null,
    val isDataSyncEnabled: Boolean = false,
    val isSignOutDialogVisible: Boolean = false,
)