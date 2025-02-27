package dev.maruffirdaus.spendly.ui.main.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import dev.maruffirdaus.spendly.ui.util.formatNumber

@Composable
fun WalletDrawer(
    wallets: List<Wallet>,
    selectedWalletId: String,
    drawerState: DrawerState,
    onDrawerItemClick: (Wallet) -> Unit,
    onAddWalletClick: () -> Unit,
    loading: Boolean,
    content: @Composable () -> Unit
) {
    ModalNavigationDrawer(
        drawerContent = {
            WalletDrawerContent(
                wallets = wallets,
                selectedWalletId = selectedWalletId,
                onDrawerItemClick = onDrawerItemClick,
                onAddWalletClick = onAddWalletClick,
                loading = loading
            )
        },
        drawerState = drawerState,
        content = content
    )
}

@Composable
private fun WalletDrawerContent(
    wallets: List<Wallet>,
    selectedWalletId: String,
    onDrawerItemClick: (Wallet) -> Unit,
    onAddWalletClick: () -> Unit,
    loading: Boolean
) {
    ModalDrawerSheet(
        windowInsets = WindowInsets(0.dp)
    ) {
        AnimatedVisibility(
            visible = loading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                        bottom = WindowInsets.navigationBars.asPaddingValues()
                            .calculateBottomPadding()
                    ),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        AnimatedVisibility(
            visible = !loading,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding(),
                    end = 12.dp,
                    bottom = WindowInsets.navigationBars.asPaddingValues().calculateBottomPadding()
                )
            ) {
                item {
                    Text(
                        text = stringResource(R.string.wallets),
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.titleSmall
                    )
                }

                items(items = wallets, key = { it.walletId }) { wallet ->
                    val isSelected = wallet.walletId == selectedWalletId

                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = wallet.title,
                                color = if (isSelected) {
                                    MaterialTheme.colorScheme.onSecondaryContainer
                                } else {
                                    MaterialTheme.colorScheme.onSurfaceVariant
                                },
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            onDrawerItemClick(wallet)
                        },
                        icon = {
                            Icon(
                                painter = if (isSelected) {
                                    painterResource(R.drawable.ic_account_balance_wallet_filled)
                                } else {
                                    painterResource(R.drawable.ic_account_balance_wallet)
                                },
                                contentDescription = stringResource(R.string.wallet)
                            )
                        },
                        badge = {
                            Text(
                                text = if (wallet.balance < 0) {
                                    buildString {
                                        append(wallet.currency)
                                        append(" -")
                                        append(formatNumber(wallet.balance * -1))
                                    }
                                } else {
                                    buildString {
                                        append(wallet.currency)
                                        append(" ")
                                        append(formatNumber(wallet.balance))
                                    }
                                },
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                style = MaterialTheme.typography.labelLarge
                            )
                        }
                    )
                }

                item {
                    NavigationDrawerItem(
                        label = {
                            Text(
                                text = stringResource(R.string.add_wallet),
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                overflow = TextOverflow.Ellipsis,
                                maxLines = 1,
                                style = MaterialTheme.typography.labelLarge
                            )
                        },
                        selected = false,
                        onClick = onAddWalletClick,
                        icon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_add),
                                contentDescription = stringResource(R.string.wallet)
                            )
                        }
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun WalletDrawerContentPreview() {
    SpendlyTheme {
        WalletDrawerContent(
            wallets = listOf(
                Wallet(
                    title = "Dana",
                    currency = "Rp",
                    balance = 500000
                ),
                Wallet(
                    title = "Gopay",
                    currency = "Rp",
                    balance = 1500000,
                    walletId = ""
                )
            ),
            selectedWalletId = "",
            onDrawerItemClick = {},
            onAddWalletClick = {},
            loading = false
        )
    }
}