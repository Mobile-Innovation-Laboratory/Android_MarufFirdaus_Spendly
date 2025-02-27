package dev.maruffirdaus.spendly.ui.signup

data class SignUpUiState(
    val email: String = "",
    val emailError: String? = null,
    val password: String = "",
    val isPasswordVisible: Boolean = false,
    val passwordError: String? = null,
    val confirmPassword: String = "",
    val isConfirmPasswordVisible: Boolean = false,
    val confirmPasswordError: String? = null,
    val isLoading: Boolean = false,
    val snackbarMessage: String? = null
)