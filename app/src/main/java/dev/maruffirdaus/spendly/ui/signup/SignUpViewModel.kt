package dev.maruffirdaus.spendly.ui.signup

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maruffirdaus.spendly.domain.usecase.auth.SignUpUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel @Inject constructor(
    private val signUpUseCase: SignUpUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(SignUpUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: SignUpEvent) {
        when (event) {
            is SignUpEvent.OnEmailChange -> {
                _uiState.update {
                    it.copy(
                        email = event.email
                    )
                }
            }

            is SignUpEvent.OnEmailError -> {
                _uiState.update {
                    it.copy(
                        emailError = event.error
                    )
                }
            }

            is SignUpEvent.OnPasswordChange -> {
                _uiState.update {
                    it.copy(
                        password = event.password
                    )
                }
            }

            is SignUpEvent.OnChangePasswordVisibilityClick -> {
                _uiState.update {
                    it.copy(
                        isPasswordVisible = !uiState.value.isPasswordVisible
                    )
                }
            }

            is SignUpEvent.OnPasswordError -> {
                _uiState.update {
                    it.copy(
                        passwordError = event.error
                    )
                }
            }

            is SignUpEvent.OnConfirmPasswordChange -> {
                _uiState.update {
                    it.copy(
                        confirmPassword = event.password
                    )
                }
            }

            is SignUpEvent.OnChangeConfirmPasswordVisibilityClick -> {
                _uiState.update {
                    it.copy(
                        isConfirmPasswordVisible = !uiState.value.isConfirmPasswordVisible
                    )
                }
            }

            is SignUpEvent.OnConfirmPasswordError -> {
                _uiState.update {
                    it.copy(
                        confirmPasswordError = event.error
                    )
                }
            }

            is SignUpEvent.OnSignUp -> {
                viewModelScope.launch {
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(
                                isLoading = true
                            )
                        }
                        signUpUseCase(
                            email = uiState.value.email,
                            password = uiState.value.password
                        ).fold(
                            onSuccess = {
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
            }

            is SignUpEvent.OnSnackbarMessageChange -> {
                _uiState.update {
                    it.copy(
                        snackbarMessage = event.message
                    )
                }
            }
        }
    }
}