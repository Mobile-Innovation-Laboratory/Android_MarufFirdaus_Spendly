package dev.maruffirdaus.spendly.ui.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.domain.usecase.auth.GetUserUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.GetDataSyncEnabledUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.SaveSelectedWalletIdUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.AddEditWalletUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val saveSelectedWalletIdUseCase: SaveSelectedWalletIdUseCase,
    private val getDataSyncEnabledUseCase: GetDataSyncEnabledUseCase,
    private val addEditWalletUseCase: AddEditWalletUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(WelcomeUiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(ExperimentalUuidApi::class)
    fun onEvent(event: WelcomeEvent) {
        when (event) {
            is WelcomeEvent.OnTitleChange -> {
                _uiState.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            is WelcomeEvent.OnCurrencyChange -> {
                _uiState.update {
                    it.copy(
                        currency = event.currency
                    )
                }
            }

            is WelcomeEvent.OnBalanceChange -> {
                _uiState.update {
                    it.copy(
                        balance = event.balance
                    )
                }
            }

            is WelcomeEvent.OnBalanceError -> {
                _uiState.update {
                    it.copy(
                        balanceError = event.error
                    )
                }
            }

            is WelcomeEvent.OnShowEnableDataSyncDialog -> {
                _uiState.update {
                    it.copy(
                        isEnableDataSyncDialogVisible = true
                    )
                }
            }

            is WelcomeEvent.OnHideEnableDataSyncDialog -> {
                _uiState.update {
                    it.copy(
                        isEnableDataSyncDialogVisible = false
                    )
                }
            }

            is WelcomeEvent.OnSaveWallet -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }

                    val walletId = Uuid.random().toString()

                    addEditWalletUseCase(
                        Wallet(
                            title = uiState.value.title,
                            currency = uiState.value.currency,
                            balance = uiState.value.balance.toLong(),
                            walletId = walletId
                        )
                    )
                    saveSelectedWalletIdUseCase(walletId)
                    event.onSuccess?.invoke()
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }

            is WelcomeEvent.OnRefreshUser -> {
                _uiState.update {
                    it.copy(
                        user = getUserUseCase()
                    )
                }
            }

            is WelcomeEvent.OnRefreshDataSyncEnabled -> {
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