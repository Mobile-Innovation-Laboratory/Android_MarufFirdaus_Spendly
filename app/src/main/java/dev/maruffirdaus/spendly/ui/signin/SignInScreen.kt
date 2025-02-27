package dev.maruffirdaus.spendly.ui.signin

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
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.Main
import dev.maruffirdaus.spendly.ui.SignIn
import dev.maruffirdaus.spendly.ui.SignUp
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun SignInScreen(
    uiState: SignInUiState,
    onEvent: (SignInEvent) -> Unit,
    shouldEnableDataSync: Boolean = false,
    shouldNavigateToMainScreen: Boolean = false,
    navController: NavController = rememberNavController()
) {
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(uiState.snackbarMessage) {
        uiState.snackbarMessage?.let { message ->
            snackbarHostState.showSnackbar(message)
            onEvent(SignInEvent.OnSnackbarMessageChange(null))
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
                        var isSignInButtonEnabled by remember { mutableStateOf(false) }

                        LaunchedEffect(uiState.email, uiState.password) {
                            isSignInButtonEnabled =
                                uiState.email.isNotBlank() && uiState.password.isNotBlank()
                        }

                        Text(
                            text = stringResource(R.string.sign_in_message),
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
                                onEvent(SignInEvent.OnEmailChange(it))
                            },
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            label = {
                                Text(stringResource(R.string.email))
                            },
                            singleLine = true
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = uiState.password,
                            onValueChange = {
                                onEvent(SignInEvent.OnPasswordChange(it))
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
                                        onEvent(SignInEvent.OnChangePasswordVisibilityClick)
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
                            visualTransformation = if (uiState.isPasswordVisible) {
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
                                        route = SignIn(
                                            shouldEnableDataSync = shouldEnableDataSync,
                                            shouldNavigateToMainScreen = shouldNavigateToMainScreen
                                        ),
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
                                        SignInEvent.OnSignIn(
                                            shouldEnableDataSync = shouldEnableDataSync,
                                            shouldNavigateToMainScreen = shouldNavigateToMainScreen,
                                            onSuccess = {
                                                if (shouldNavigateToMainScreen) {
                                                    navController.navigate(Main) {
                                                        popUpTo(0) {
                                                            inclusive = true

                                                        }
                                                    }
                                                } else {
                                                    navController.popBackStack(
                                                        route = SignIn(
                                                            shouldEnableDataSync = shouldEnableDataSync,
                                                            shouldNavigateToMainScreen = false
                                                        ),
                                                        inclusive = true
                                                    )
                                                }
                                            }
                                        )
                                    )
                                },
                                modifier = Modifier.weight(1f),
                                enabled = isSignInButtonEnabled
                            ) {
                                Text(stringResource(R.string.sign_in))
                            }
                        }
                        Spacer(Modifier.height(32.dp))
                        Text(
                            text = stringResource(R.string.dont_have_an_account),
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall
                        )
                        Spacer(Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                navController.navigate(SignUp)
                            }
                        ) {
                            Text(stringResource(R.string.sign_up))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun SignInScreenPreview() {
    SpendlyTheme {
        SignInScreen(
            uiState = SignInUiState(),
            onEvent = {}
        )
    }
}