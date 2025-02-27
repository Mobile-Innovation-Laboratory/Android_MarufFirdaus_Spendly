package dev.maruffirdaus.spendly.ui.signup

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.SignUp
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun SignUpScreen(
    uiState: SignUpUiState,
    onEvent: (SignUpEvent) -> Unit,
    navController: NavController = rememberNavController()
) {
    val context = LocalContext.current

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(SignUpEvent.OnSnackbarMessageChange(null))
        }
    }

    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    modifier = Modifier.offset(
                        y = -WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                    )
                )
            }
        }
    ) { innerPadding ->
        BoxWithConstraints {
            Box(
                modifier = Modifier
                    .imePadding()
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                AnimatedVisibility(
                    visible = uiState.isLoading,
                    modifier = Modifier
                        .padding(innerPadding)
                        .align(Alignment.Center),
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    CircularProgressIndicator()
                }
                AnimatedVisibility(
                    visible = !uiState.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(min = this@BoxWithConstraints.maxHeight)
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        var isEmailError by remember { mutableStateOf(false) }
                        var isPasswordError by remember { mutableStateOf(false) }
                        var isConfirmPasswordError by remember { mutableStateOf(false) }
                        var isSignUpButtonEnabled by remember { mutableStateOf(false) }

                        LaunchedEffect(uiState.emailError) {
                            isEmailError = uiState.emailError != null
                        }

                        LaunchedEffect(uiState.passwordError) {
                            isPasswordError = uiState.passwordError != null
                        }

                        LaunchedEffect(uiState.confirmPasswordError) {
                            isConfirmPasswordError = uiState.confirmPasswordError != null
                        }

                        LaunchedEffect(
                            uiState.email,
                            isEmailError,
                            uiState.password,
                            isPasswordError,
                            uiState.confirmPassword,
                            isConfirmPasswordError
                        ) {
                            isSignUpButtonEnabled =
                                uiState.email.isNotBlank() && !isEmailError && uiState.password.isNotBlank() && !isPasswordError && uiState.confirmPassword.isNotBlank() && !isConfirmPasswordError
                        }

                        Text(
                            text = stringResource(R.string.sign_up_message),
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(Modifier.height(64.dp))
                        OutlinedTextField(
                            value = uiState.email,
                            onValueChange = {
                                onEvent(SignUpEvent.OnEmailChange(it))
                                onEvent(
                                    SignUpEvent.OnEmailError(
                                        if (!Patterns.EMAIL_ADDRESS.matcher(it).matches()) {
                                            context.getString(R.string.email_error_message)
                                        } else {
                                            null
                                        }
                                    )
                                )
                            },
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            label = {
                                Text(stringResource(R.string.email))
                            },
                            supportingText = if (uiState.emailError != null) {
                                {
                                    Text(uiState.emailError)
                                }
                            } else {
                                null
                            },
                            isError = isEmailError,
                            singleLine = true
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = {
                                onEvent(SignUpEvent.OnPasswordChange(it))
                                onEvent(
                                    SignUpEvent.OnPasswordError(
                                        if (it.length < 8) {
                                            context.getString(R.string.password_error_message)
                                        } else {
                                            null
                                        }
                                    )
                                )
                                onEvent(
                                    SignUpEvent.OnConfirmPasswordError(
                                        if (it != uiState.confirmPassword) {
                                            context.getString(R.string.confirm_password_error_message)
                                        } else {
                                            null
                                        }
                                    )
                                )
                            },
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            label = {
                                Text(stringResource(R.string.password))
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        onEvent(SignUpEvent.OnChangePasswordVisibilityClick)
                                    }
                                ) {
                                    Icon(
                                        painter = if (uiState.isPasswordVisible) {
                                            painterResource(R.drawable.ic_visibility_off)
                                        } else {
                                            painterResource(R.drawable.ic_visibility)
                                        },
                                        contentDescription = stringResource(R.string.show_hide_password)
                                    )
                                }
                            },
                            supportingText = if (uiState.passwordError != null) {
                                {
                                    Text(uiState.passwordError)
                                }
                            } else {
                                null
                            },
                            isError = isPasswordError,
                            visualTransformation = if (uiState.isPasswordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = uiState.confirmPassword,
                            onValueChange = {
                                onEvent(SignUpEvent.OnConfirmPasswordChange(it))
                                onEvent(
                                    SignUpEvent.OnConfirmPasswordError(
                                        if (it != uiState.password) {
                                            context.getString(R.string.confirm_password_error_message)
                                        } else {
                                            null
                                        }
                                    )
                                )
                            },
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            label = {
                                Text(stringResource(R.string.confirm_password))
                            },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        onEvent(SignUpEvent.OnChangeConfirmPasswordVisibilityClick)
                                    }
                                ) {
                                    Icon(
                                        painter = if (uiState.isConfirmPasswordVisible) {
                                            painterResource(R.drawable.ic_visibility_off)
                                        } else {
                                            painterResource(R.drawable.ic_visibility)
                                        },
                                        contentDescription = stringResource(R.string.show_hide_password)
                                    )
                                }
                            },
                            supportingText = if (uiState.confirmPasswordError != null) {
                                {
                                    Text(uiState.confirmPasswordError)
                                }
                            } else {
                                null
                            },
                            isError = isConfirmPasswordError,
                            visualTransformation = if (uiState.isConfirmPasswordVisible) {
                                VisualTransformation.None
                            } else {
                                PasswordVisualTransformation()
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            singleLine = true
                        )
                        Spacer(Modifier.height(64.dp))
                        Row(
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth()
                        ) {
                            FilledTonalButton(
                                onClick = {
                                    navController.popBackStack(
                                        route = SignUp,
                                        inclusive = true
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            ) {
                                Text(stringResource(R.string.cancel))
                            }
                            Spacer(Modifier.width(8.dp))
                            Button(
                                onClick = {
                                    onEvent(
                                        SignUpEvent.OnSignUp(
                                            onSuccess = {
                                                navController.popBackStack(
                                                    route = SignUp,
                                                    inclusive = true
                                                )
                                            }
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                enabled = isSignUpButtonEnabled
                            ) {
                                Text(stringResource(R.string.sign_up))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SignUpScreenPreview() {
    SpendlyTheme {
        SignUpScreen(
            uiState = SignUpUiState(),
            onEvent = {}
        )
    }
}