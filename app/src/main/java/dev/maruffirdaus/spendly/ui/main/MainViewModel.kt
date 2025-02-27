package dev.maruffirdaus.spendly.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maruffirdaus.spendly.domain.usecase.auth.GetUserUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.GetDataSyncEnabledUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.GetWalletsUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.SyncWalletsUseCase
import dev.maruffirdaus.spendly.ui.util.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getDataSyncEnabledUseCase: GetDataSyncEnabledUseCase,
    private val getWalletsUseCase: GetWalletsUseCase,
    private val syncWalletsUseCase: SyncWalletsUseCase,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private val _uiState = MutableStateFlow(MainUiState())
    val uiState = _uiState.asStateFlow()

    private val isConnected = connectivityObserver.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.OnNavItemClick -> {
                _uiState.update {
                    it.copy(
                        selectedNavItem = event.navItem
                    )
                }
            }

            is MainEvent.OnMenuClick -> {
                _uiState.update {
                    it.copy(
                        isMenuVisible = !uiState.value.isMenuVisible
                    )
                }
            }

            is MainEvent.OnMenuVisibilityChange -> {
                _uiState.update {
                    it.copy(
                        isMenuVisible = event.visibility
                    )
                }
            }

            is MainEvent.OnSelectYearClick -> {
                _uiState.update {
                    it.copy(
                        isYearPickerDialogVisible = true
                    )
                }
            }

            is MainEvent.OnDismissSelectYear -> {
                _uiState.update {
                    it.copy(
                        isYearPickerDialogVisible = false
                    )
                }
            }

            is MainEvent.OnRefreshWallets -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isWalletsLoading = true
                        )
                    }

                    if (isConnected.first()) {
                        val user = getUserUseCase()

                        if (user != null && getDataSyncEnabledUseCase()) {
                            syncWalletsUseCase(user.userId)
                        }
                    }

                    val wallets = getWalletsUseCase()

                    _uiState.update {
                        it.copy(
                            wallets = wallets,
                            isWalletsLoading = false
                        )
                    }
                }
            }
        }
    }
}