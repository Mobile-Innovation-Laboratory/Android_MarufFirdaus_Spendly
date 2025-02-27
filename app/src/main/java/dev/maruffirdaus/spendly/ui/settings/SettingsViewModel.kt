package dev.maruffirdaus.spendly.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maruffirdaus.spendly.domain.usecase.auth.GetUserUseCase
import dev.maruffirdaus.spendly.domain.usecase.auth.SignOutUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.GetDataSyncEnabledUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.SaveDataSyncEnabledUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val signOutUseCase: SignOutUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val saveDataSyncEnabledUseCase: SaveDataSyncEnabledUseCase,
    private val getDataSyncEnabledUseCase: GetDataSyncEnabledUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SettingsUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SettingsEvent) {
        when (event) {
            is SettingsEvent.OnShowSignOutDialog -> {
                _uiState.update {
                    it.copy(
                        isSignOutDialogVisible = true
                    )
                }
            }

            is SettingsEvent.OnHideSignOutDialog -> {
                _uiState.update {
                    it.copy(
                        isSignOutDialogVisible = false
                    )
                }
            }

            is SettingsEvent.OnSignOut -> {
                viewModelScope.launch {
                    signOutUseCase().onSuccess {
                        event.onSuccess?.invoke()
                    }
                }
            }

            is SettingsEvent.OnDataSyncEnabledChange -> {
                viewModelScope.launch {
                    saveDataSyncEnabledUseCase(event.isEnabled)
                    _uiState.update {
                        it.copy(
                            isDataSyncEnabled = event.isEnabled
                        )
                    }
                }
            }

            is SettingsEvent.OnRefreshUser -> {
                _uiState.update {
                    it.copy(
                        user = getUserUseCase()
                    )
                }
            }

            is SettingsEvent.OnRefreshDataSyncEnabled -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isDataSyncEnabled = getDataSyncEnabledUseCase()
                        )
                    }
                }
            }
        }
    }
}