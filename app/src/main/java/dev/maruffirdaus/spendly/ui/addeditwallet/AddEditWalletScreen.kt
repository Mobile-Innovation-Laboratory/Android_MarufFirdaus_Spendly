package dev.maruffirdaus.spendly.ui.addeditwallet

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.AddEditWallet
import dev.maruffirdaus.spendly.ui.addeditwallet.component.AddEditWalletTopAppBar
import dev.maruffirdaus.spendly.ui.common.component.TopGradient
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun AddEditWalletScreen(
    uiState: AddEditWalletUiState,
    onEvent: (AddEditWalletEvent) -> Unit,
    walletId: String? = null,
    navController: NavController = rememberNavController()
) {
    val context = LocalContext.current
    var isSaveButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.title, uiState.currency, uiState.balance) {
        isSaveButtonEnabled =
            uiState.title.isNotBlank() && uiState.currency.isNotBlank() && uiState.balance.isNotBlank()
    }

    LaunchedEffect(Unit) {
        if (walletId != null) {
            onEvent(AddEditWalletEvent.OnRefreshWallet(walletId))
        }
    }

    Scaffold(
        topBar = {
            AddEditWalletTopAppBar(
                isEdit = walletId != null,
                isSaveButtonEnabled = isSaveButtonEnabled,
                onCloseClick = {
                    navController.popBackStack(
                        route = AddEditWallet(walletId),
                        inclusive = true
                    )
                },
                onSaveClick = {
                    if (uiState.balance.toLongOrNull() != null) {
                        onEvent(
                            AddEditWalletEvent.OnSaveWallet(
                                walletId = walletId,
                                onSuccess = {
                                    navController.popBackStack(
                                        route = AddEditWallet(walletId),
                                        inclusive = true
                                    )
                                }
                            )
                        )
                    } else {
                        onEvent(
                            AddEditWalletEvent.OnBalanceError(
                                context.getString(R.string.balance_input_error_message)
                            )
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            TopGradient(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .height(16.dp)
                    .zIndex(1f)
            )
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
                        .imePadding()
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var isBalanceError by remember { mutableStateOf(false) }

                    LaunchedEffect(uiState.balanceError) {
                        isBalanceError = uiState.balanceError != null
                    }

                    OutlinedTextField(
                        value = uiState.title,
                        onValueChange = {
                            onEvent(AddEditWalletEvent.OnTitleChange(it))
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
                            onEvent(AddEditWalletEvent.OnCurrencyChange(it))
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

                    if (walletId == null) {
                        Spacer(Modifier.height(16.dp))
                        OutlinedTextField(
                            value = uiState.balance,
                            onValueChange = {
                                onEvent(AddEditWalletEvent.OnBalanceChange(it))
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
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun AddEditWalletScreenPreview() {
    SpendlyTheme {
        AddEditWalletScreen(
            uiState = AddEditWalletUiState(),
            onEvent = {}
        )
    }
}