package dev.maruffirdaus.spendly.ui.welcome

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.Main
import dev.maruffirdaus.spendly.ui.SignIn
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun WelcomeScreen(
    uiState: WelcomeUiState,
    onEvent: (WelcomeEvent) -> Unit,
    navController: NavController = rememberNavController()
) {
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        onEvent(WelcomeEvent.OnRefreshUser)
        onEvent(WelcomeEvent.OnRefreshDataSyncEnabled)
    }

    Scaffold { innerPadding ->
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
                        var isBalanceError by remember { mutableStateOf(false) }
                        var isSaveButtonEnabled by remember { mutableStateOf(false) }

                        LaunchedEffect(uiState.balanceError) {
                            isBalanceError = uiState.balanceError != null
                        }

                        LaunchedEffect(uiState.title, uiState.currency, uiState.balance) {
                            isSaveButtonEnabled =
                                uiState.title.isNotBlank() && uiState.currency.isNotBlank() && uiState.balance.isNotBlank()
                        }

                        Text(
                            text = stringResource(
                                R.string.welcome_message,
                                stringResource(R.string.app_name)
                            ),
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.displayMedium
                        )
                        Spacer(Modifier.height(64.dp))
                        Text(
                            text = stringResource(R.string.add_first_wallet_message),
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(Modifier.height(24.dp))
                        OutlinedTextField(
                            value = uiState.title,
                            onValueChange = {
                                onEvent(WelcomeEvent.OnTitleChange(it))
                            },
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            label = {
                                Text(stringResource(R.string.title))
                            },
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                            singleLine = true
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = uiState.currency,
                            onValueChange = {
                                onEvent(WelcomeEvent.OnCurrencyChange(it))
                            },
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            label = {
                                Text(stringResource(R.string.currency))
                            },
                            keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                            singleLine = true
                        )
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = uiState.balance,
                            onValueChange = {
                                onEvent(WelcomeEvent.OnBalanceChange(it))
                            },
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            label = {
                                Text(stringResource(R.string.balance))
                            },
                            prefix = {
                                Text(uiState.currency)
                            },
                            supportingText = if (uiState.balanceError != null) {
                                {
                                    Text(uiState.balanceError)
                                }
                            } else {
                                null
                            },
                            isError = isBalanceError,
                            keyboardOptions = KeyboardOptions(
                                capitalization = KeyboardCapitalization.Sentences,
                                keyboardType = KeyboardType.Number
                            ),
                            singleLine = true
                        )
                        Spacer(Modifier.height(64.dp))
                        Button(
                            onClick = {
                                if (uiState.balance.toLongOrNull() != null) {
                                    if (uiState.isDataSyncEnabled) {
                                        onEvent(
                                            WelcomeEvent.OnSaveWallet(
                                                onSuccess = {
                                                    navController.navigate(Main) {
                                                        popUpTo(
                                                            navController.currentBackStackEntry?.destination?.route
                                                                ?: return@navigate
                                                        ) {
                                                            inclusive = true
                                                        }
                                                    }
                                                }
                                            )
                                        )
                                    } else {
                                        onEvent(WelcomeEvent.OnShowEnableDataSyncDialog)
                                    }
                                } else {
                                    onEvent(
                                        WelcomeEvent.OnBalanceError(
                                            context.getString(R.string.balance_input_error_message)
                                        )
                                    )
                                }
                            },
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            enabled = isSaveButtonEnabled
                        ) {
                            Text(stringResource(R.string.next))
                        }
                        Spacer(Modifier.height(8.dp))
                        TextButton(
                            onClick = {
                                navController.navigate(
                                    SignIn(
                                        shouldEnableDataSync = true,
                                        shouldNavigateToMainScreen = true
                                    )
                                )
                            },
                            enabled = uiState.user == null
                        ) {
                            Text(stringResource(R.string.restore_previous_data))
                        }
                    }
                }
            }
        }

        if (uiState.isEnableDataSyncDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    onEvent(WelcomeEvent.OnHideEnableDataSyncDialog)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(WelcomeEvent.OnHideEnableDataSyncDialog)

                            if (uiState.user == null) {
                                navController.navigate(
                                    SignIn(
                                        shouldEnableDataSync = true,
                                        shouldNavigateToMainScreen = false
                                    )
                                )
                            } else {
                                onEvent(
                                    WelcomeEvent.OnSaveWallet(
                                        onSuccess = {
                                            navController.navigate(Main) {
                                                popUpTo(
                                                    navController.currentBackStackEntry?.destination?.route
                                                        ?: return@navigate
                                                ) {
                                                    inclusive = true
                                                }
                                            }
                                        }
                                    )
                                )
                            }
                        }
                    ) {
                        Text(stringResource(R.string.yes))
                    }
                },
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .fillMaxWidth(),
                dismissButton = {
                    TextButton(
                        onClick = {
                            onEvent(WelcomeEvent.OnHideEnableDataSyncDialog)
                            onEvent(
                                WelcomeEvent.OnSaveWallet(
                                    onSuccess = {
                                        navController.navigate(Main) {
                                            popUpTo(
                                                navController.currentBackStackEntry?.destination?.route
                                                    ?: return@navigate
                                            ) {
                                                inclusive = true
                                            }
                                        }
                                    }
                                )
                            )
                        }
                    ) {
                        Text(stringResource(R.string.no))
                    }
                },
                title = {
                    Text(stringResource(R.string.enable_data_sync_title))
                },
                text = {
                    Text(stringResource(R.string.enable_data_sync_message))
                }
            )
        }
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    SpendlyTheme {
        WelcomeScreen(
            uiState = WelcomeUiState(),
            onEvent = {}
        )
    }
}