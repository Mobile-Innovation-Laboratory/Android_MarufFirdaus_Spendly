package dev.maruffirdaus.spendly.ui.signin

sealed class SignInEvent {
    data class OnEmailChange(val email: String) : SignInEvent()

    data class OnPasswordChange(val password: String) : SignInEvent()

    data object OnChangePasswordVisibilityClick : SignInEvent()

    data class OnSignIn(
        val shouldEnableDataSync: Boolean,
        val shouldNavigateToMainScreen: Boolean,
        val onSuccess: (() -> Unit)? = null
    ) : SignInEvent()

    data class OnSnackbarMessageChange(val message: String?) : SignInEvent()
}