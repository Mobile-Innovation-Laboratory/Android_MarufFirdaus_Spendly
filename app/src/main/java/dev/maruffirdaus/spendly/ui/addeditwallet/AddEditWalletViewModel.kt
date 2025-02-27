package dev.maruffirdaus.spendly.ui.addeditwallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.domain.usecase.auth.GetUserUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.GetDataSyncEnabledUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.AddEditWalletUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.GetWalletUseCase
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
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
class AddEditWalletViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getDataSyncEnabledUseCase: GetDataSyncEnabledUseCase,
    private val addEditWalletUseCase: AddEditWalletUseCase,
    private val getWalletUseCase: GetWalletUseCase,
    private val syncWalletsUseCase: SyncWalletsUseCase,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditWalletUiState())
    val uiState = _uiState.asStateFlow()

    private val isConnected = connectivityObserver.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    @OptIn(ExperimentalUuidApi::class)
    fun onEvent(event: AddEditWalletEvent) {
        when (event) {
            is AddEditWalletEvent.OnSaveWallet -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    addEditWalletUseCase(
                        Wallet(
                            title = uiState.value.title,
                            currency = uiState.value.currency,
                            balance = uiState.value.balance.toLong(),
                            walletId = event.walletId ?: Uuid.random().toString()
                        )
                    )

                    if (isConnected.first()) {
                        val user = getUserUseCase()

                        if (user != null && getDataSyncEnabledUseCase()) {
                            syncWalletsUseCase(user.userId)
                        }
                    }

                    event.onSuccess?.invoke()
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }

            is AddEditWalletEvent.OnTitleChange -> {
                _uiState.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            is AddEditWalletEvent.OnCurrencyChange -> {
                _uiState.update {
                    it.copy(
                        currency = event.currency
                    )
                }
            }

            is AddEditWalletEvent.OnBalanceChange -> {
                _uiState.update {
                    it.copy(
                        balance = event.balance
                    )
                }
            }

            is AddEditWalletEvent.OnBalanceError -> {
                _uiState.update {
                    it.copy(
                        balanceError = event.error
                    )
                }
            }

            is AddEditWalletEvent.OnRefreshWallet -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }

                    val wallet = getWalletUseCase(event.walletId)

                    _uiState.update {
                        it.copy(
                            title = wallet.title,
                            currency = wallet.currency,
                            balance = wallet.balance.toString(),
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}