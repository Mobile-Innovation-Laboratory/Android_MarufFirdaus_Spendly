package dev.maruffirdaus.spendly.ui.signup

sealed class SignUpEvent {
    data class OnEmailChange(val email: String) : SignUpEvent()

    data class OnEmailError(val error: String?) : SignUpEvent()

    data class OnPasswordChange(val password: String) : SignUpEvent()

    data object OnChangePasswordVisibilityClick : SignUpEvent()

    data class OnPasswordError(val error: String?) : SignUpEvent()

    data class OnConfirmPasswordChange(val password: String) : SignUpEvent()

    data object OnChangeConfirmPasswordVisibilityClick : SignUpEvent()

    data class OnConfirmPasswordError(val error: String?) : SignUpEvent()

    data class OnSignUp(val onSuccess: (() -> Unit)? = null) : SignUpEvent()

    data class OnSnackbarMessageChange(val message: String?) : SignUpEvent()
}