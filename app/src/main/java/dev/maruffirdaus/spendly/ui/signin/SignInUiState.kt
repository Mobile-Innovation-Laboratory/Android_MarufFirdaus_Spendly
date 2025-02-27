package dev.maruffirdaus.spendly.ui.signin

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null
)