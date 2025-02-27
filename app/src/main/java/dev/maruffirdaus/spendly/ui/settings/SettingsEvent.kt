package dev.maruffirdaus.spendly.ui.settings

sealed class SettingsEvent {
    data object OnShowSignOutDialog : SettingsEvent()
    data object OnHideSignOutDialog : SettingsEvent()

    data class OnSignOut(val onSuccess: (() -> Unit)? = null) : SettingsEvent()

    data class OnDataSyncEnabledChange(val isEnabled: Boolean) : SettingsEvent()

    data object OnRefreshUser : SettingsEvent()

    data object OnRefreshDataSyncEnabled : SettingsEvent()
}