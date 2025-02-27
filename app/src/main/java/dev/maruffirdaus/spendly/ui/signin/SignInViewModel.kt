package dev.maruffirdaus.spendly.ui.signin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maruffirdaus.spendly.domain.usecase.auth.GetUserUseCase
import dev.maruffirdaus.spendly.domain.usecase.auth.SignInUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.SaveDataSyncEnabledUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.SaveSelectedWalletIdUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.GetWalletsUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.SyncWalletsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignInViewModel @Inject constructor(
    private val signInUseCase: SignInUseCase,
    private val getUserUseCase: GetUserUseCase,
    private val saveSelectedWalletIdUseCase: SaveSelectedWalletIdUseCase,
    private val saveDataSyncEnabledUseCase: SaveDataSyncEnabledUseCase,
    private val syncWalletsUseCase: SyncWalletsUseCase,
    private val getWalletsUseCase: GetWalletsUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignInUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SignInEvent) {
        when (event) {
            is SignInEvent.OnEmailChange -> {
                _uiState.update {
                    it.copy(
                        email = event.email
                    )
                }
            }

            is SignInEvent.OnPasswordChange -> {
                _uiState.update {
                    it.copy(
                        password = event.password
                    )
                }
            }

            is SignInEvent.OnChangePasswordVisibilityClick -> {
                _uiState.update {
                    it.copy(
                        isPasswordVisible = !uiState.value.isPasswordVisible
                    )
                }
            }

            is SignInEvent.OnSignIn -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    signInUseCase(
                        email = uiState.value.email,
                        password = uiState.value.password
                    ).fold(
                        onSuccess = {
                            saveDataSyncEnabledUseCase(event.shouldEnableDataSync)

                            if (event.shouldNavigateToMainScreen) {
                                val user = getUserUseCase()

                                if (user != null) {
                                    syncWalletsUseCase(user.userId)

                                    val wallets = getWalletsUseCase()

                                    saveSelectedWalletIdUseCase(wallets.firstOrNull()?.walletId)
                                }
                            }

                            event.onSuccess?.invoke()
                        },
                        onFailure = { e ->
                            _uiState.update {
                                it.copy(
                                    snackbarMessage = e.localizedMessage
                                )
                            }
                        }
                    )
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }

            is SignInEvent.OnSnackbarMessageChange -> {
                _uiState.update {
                    it.copy(
                        snackbarMessage = event.message
                    )
                }
            }
        }
    }
}