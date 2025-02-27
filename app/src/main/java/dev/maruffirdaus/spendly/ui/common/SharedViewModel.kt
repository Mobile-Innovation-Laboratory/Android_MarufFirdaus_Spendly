package dev.maruffirdaus.spendly.ui.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maruffirdaus.spendly.domain.usecase.preference.GetSelectedWalletIdUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.SaveSelectedWalletIdUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.GetWalletUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val saveSelectedWalletIdUseCase: SaveSelectedWalletIdUseCase,
    private val getSelectedWalletIdUseCase: GetSelectedWalletIdUseCase,
    private val getWalletUseCase: GetWalletUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SharedUiState())
    val uiState = _uiState.asStateFlow()

    init {
        runBlocking {
            val selectedWalletId = getSelectedWalletIdUseCase()

            if (selectedWalletId != null) {
                _uiState.update {
                    it.copy(
                        selectedWallet = getWalletUseCase(selectedWalletId)
                    )
                }
            }
        }
    }

    fun onEvent(event: SharedEvent) {
        when (event) {
            is SharedEvent.OnSelectedWalletChange -> {
                viewModelScope.launch {
                    saveSelectedWalletIdUseCase(event.wallet.walletId)
                    _uiState.update {
                        it.copy(
                            selectedWallet = event.wallet
                        )
                    }
                }
            }

            is SharedEvent.OnSelectedYearChange -> {
                _uiState.update {
                    it.copy(
                        selectedYear = event.year
                    )
                }
            }

            is SharedEvent.OnSnackbarMessageChange -> {
                _uiState.update {
                    it.copy(
                        snackbarMessage = event.message
                    )
                }
            }

            is SharedEvent.OnRefreshSelectedWallet -> {
                viewModelScope.launch {
                    uiState.value.selectedWallet?.let { wallet ->
                        _uiState.update {
                            it.copy(
                                selectedWallet = getWalletUseCase(wallet.walletId)
                            )
                        }
                    }
                }
            }
        }
    }
}