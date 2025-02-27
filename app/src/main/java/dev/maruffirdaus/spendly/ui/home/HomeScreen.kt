package dev.maruffirdaus.spendly.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.ui.AddEditWallet
import dev.maruffirdaus.spendly.ui.Settings
import dev.maruffirdaus.spendly.ui.common.SharedEvent
import dev.maruffirdaus.spendly.ui.common.SharedUiState
import dev.maruffirdaus.spendly.ui.common.component.BottomGradient
import dev.maruffirdaus.spendly.ui.common.component.TopGradient
import dev.maruffirdaus.spendly.ui.home.component.GraphCard
import dev.maruffirdaus.spendly.ui.home.component.HomeTopAppBar
import dev.maruffirdaus.spendly.ui.home.component.WalletCard
import dev.maruffirdaus.spendly.ui.theme.LocalExtendedColorScheme
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun HomeScreen(
    hasAnotherWallet: Boolean,
    sharedUiState: SharedUiState,
    uiState: HomeUiState,
    onSharedEvent: (SharedEvent) -> Unit,
    onEvent: (HomeEvent) -> Unit,
    onMenuClick: (() -> Unit)?,
    onSelectYearClick: () -> Unit,
    onRefreshWallets: () -> Unit,
    modifier: Modifier = Modifier,
    compactScreen: Boolean = true,
    navController: NavController = rememberNavController()
) {
    val context = LocalContext.current

    Column(
        modifier = modifier.fillMaxSize(),
    ) {
        val scrollState = rememberScrollState(uiState.scrollPosition)

        LaunchedEffect(scrollState.value) {
            onEvent(HomeEvent.OnScrollPositionChange(scrollState.value))
        }

        LaunchedEffect(sharedUiState.selectedWallet, sharedUiState.selectedYear) {
            sharedUiState.selectedWallet?.walletId?.let { walletId ->
                onEvent(
                    HomeEvent.OnRefreshGraphData(
                        walletId = walletId,
                        year = sharedUiState.selectedYear
                    )
                )
            }
        }

        HomeTopAppBar(
            onMenuClick = onMenuClick,
            onSettingsClick = {
                navController.navigate(Settings)
            }
        )
        Box(
            modifier = Modifier.weight(1f)
        ) {
            TopGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .zIndex(1f)
            )
            this@Column.AnimatedVisibility(
                visible = uiState.isLoading,
                modifier = Modifier
                    .let {
                        if (compactScreen) {
                            it
                        } else {
                            it.padding(
                                bottom = WindowInsets.navigationBars.asPaddingValues()
                                    .calculateBottomPadding()
                            )
                        }
                    }
                    .align(Alignment.Center),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CircularProgressIndicator()
            }
            this@Column.AnimatedVisibility(
                visible = !uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp + WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding()
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    sharedUiState.selectedWallet?.let { wallet ->
                        Box(
                            modifier = Modifier.widthIn(max = 600.dp)
                        ) {
                            var isActionsVisible by remember { mutableStateOf(false) }

                            WalletCard(
                                title = wallet.title,
                                currency = wallet.currency,
                                balance = wallet.balance,
                                onClick = {
                                    isActionsVisible = true
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                            DropdownMenu(
                                expanded = isActionsVisible,
                                onDismissRequest = {
                                    isActionsVisible = false
                                }
                            ) {
                                DropdownMenuItem(
                                    text = {
                                        Text(stringResource(R.string.edit))
                                    },
                                    onClick = {
                                        navController.navigate(AddEditWallet(wallet.walletId))
                                        isActionsVisible = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_edit),
                                            contentDescription = null
                                        )
                                    }
                                )
                                DropdownMenuItem(
                                    text = {
                                        Text(stringResource(R.string.delete))
                                    },
                                    onClick = {
                                        onEvent(HomeEvent.OnDeleteWalletClick(wallet))
                                        isActionsVisible = false
                                    },
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_delete),
                                            contentDescription = null
                                        )
                                    }
                                )
                            }
                        }
                        Spacer(Modifier.height(24.dp))
                        Row(
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.overview),
                                color = MaterialTheme.colorScheme.onSurface,
                                style = MaterialTheme.typography.titleMedium
                            )
                            TextButton(
                                onClick = onSelectYearClick
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_calendar_today),
                                    contentDescription = stringResource(R.string.year_picker)
                                )
                                Spacer(Modifier.width(8.dp))
                                Text(sharedUiState.selectedYear)
                            }
                        }
                        Spacer(Modifier.height(16.dp))
                        GraphCard(
                            currency = wallet.currency,
                            data = uiState.incomeData,
                            label = stringResource(R.string.income),
                            color = LocalExtendedColorScheme.current.income,
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth()
                        )
                        Spacer(Modifier.height(16.dp))
                        GraphCard(
                            currency = wallet.currency,
                            data = uiState.expenseData,
                            label = stringResource(R.string.expense),
                            color = LocalExtendedColorScheme.current.expense,
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
            BottomGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .align(Alignment.BottomStart)
                    .zIndex(1f)
            )
        }
    }

    if (uiState.isDeleteWalletDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                onEvent(HomeEvent.OnCancelDeleteWallet)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        if (hasAnotherWallet) {
                            onEvent(
                                HomeEvent.OnConfirmDeleteWallet(
                                    onSuccess = {
                                        onRefreshWallets()
                                    }
                                )
                            )
                        } else {
                            onSharedEvent(
                                SharedEvent.OnSnackbarMessageChange(
                                    context.getString(R.string.delete_wallet_error_message)
                                )
                            )
                            onEvent(HomeEvent.OnCancelDeleteWallet)
                        }
                    }
                ) {
                    Text(stringResource(R.string.delete))
                }
            },
            modifier = Modifier
                .widthIn(max = 320.dp)
                .fillMaxWidth(),
            dismissButton = {
                TextButton(
                    onClick = {
                        onEvent(HomeEvent.OnCancelDeleteWallet)
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = {
                Text(stringResource(R.string.delete_wallet_title))
            },
            text = {
                Text(stringResource(R.string.delete_wallet_message))
            }
        )
    }
}

@Preview
@Composable
private fun HomeScreenPreview() {
    SpendlyTheme {
        Surface {
            HomeScreen(
                hasAnotherWallet = true,
                sharedUiState = SharedUiState(
                    selectedWallet = Wallet(
                        title = "Dana",
                        currency = "Rp",
                        balance = 1500000,
                        walletId = ""
                    )
                ),
                uiState = HomeUiState(),
                onSharedEvent = {},
                onEvent = {},
                onMenuClick = {},
                onSelectYearClick = {},
                onRefreshWallets = {}
            )
        }
    }
}