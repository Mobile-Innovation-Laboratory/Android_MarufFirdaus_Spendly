package dev.maruffirdaus.spendly.ui.main

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.ui.AddEditWallet
import dev.maruffirdaus.spendly.ui.Welcome
import dev.maruffirdaus.spendly.ui.common.SharedEvent
import dev.maruffirdaus.spendly.ui.common.SharedUiState
import dev.maruffirdaus.spendly.ui.common.component.LeftGradient
import dev.maruffirdaus.spendly.ui.main.component.AddFab
import dev.maruffirdaus.spendly.ui.main.component.MainNavBar
import dev.maruffirdaus.spendly.ui.main.component.MainNavRail
import dev.maruffirdaus.spendly.ui.main.component.WalletDrawer
import dev.maruffirdaus.spendly.ui.main.component.YearPickerDialog
import dev.maruffirdaus.spendly.ui.main.constant.MainNavItems
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import kotlinx.coroutines.launch

@Composable
fun MainScreen(
    sharedUiState: SharedUiState,
    uiState: MainUiState,
    onSharedEvent: (SharedEvent) -> Unit,
    onEvent: (MainEvent) -> Unit,
    homeScreen: @Composable (Modifier) -> Unit,
    activitiesScreen: @Composable (Modifier) -> Unit,
    dealsScreen: @Composable (Modifier) -> Unit,
    onAddActivityClick: () -> Unit,
    compactScreen: Boolean = true,
    navController: NavController = rememberNavController()
) {
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        onEvent(MainEvent.OnRefreshWallets)
    }

    LaunchedEffect(uiState.isMenuVisible) {
        if (uiState.isMenuVisible) {
            onEvent(MainEvent.OnRefreshWallets)
        }

        drawerState.apply {
            if (uiState.isMenuVisible) open() else close()
        }
    }

    LaunchedEffect(drawerState.isOpen) {
        onEvent(MainEvent.OnMenuVisibilityChange(drawerState.isOpen))
    }

    BackHandler(drawerState.isOpen) {
        scope.launch {
            drawerState.close()
        }
    }

    LaunchedEffect(uiState.wallets) {
        if (uiState.wallets.isNotEmpty() && !uiState.wallets.contains(sharedUiState.selectedWallet)) {
            onSharedEvent(
                SharedEvent.OnSelectedWalletChange(
                    uiState.wallets.find { it.walletId == sharedUiState.selectedWallet?.walletId }
                        ?: uiState.wallets.first()
                )
            )
        }
    }

    LaunchedEffect(sharedUiState.selectedWallet) {
        if (sharedUiState.selectedWallet == null) {
            navController.navigate(Welcome) {
                popUpTo(0) {
                    inclusive = true
                }
            }
        }
    }
    if (sharedUiState.selectedWallet != null) {
        WalletDrawer(
            wallets = uiState.wallets,
            selectedWalletId = sharedUiState.selectedWallet.walletId,
            drawerState = drawerState,
            onDrawerItemClick = { wallet ->
                onSharedEvent(SharedEvent.OnSelectedWalletChange(wallet))
                onEvent(MainEvent.OnMenuVisibilityChange(false))
            },
            onAddWalletClick = {
                navController.navigate(AddEditWallet(null))
            },
            loading = uiState.isWalletsLoading
        ) {
            val snackbarHostState = remember { SnackbarHostState() }

            LaunchedEffect(sharedUiState.snackbarMessage) {
                sharedUiState.snackbarMessage?.let { message ->
                    snackbarHostState.showSnackbar(message)
                    onSharedEvent(SharedEvent.OnSnackbarMessageChange(null))
                }
            }

            Scaffold(
                bottomBar = {
                    if (compactScreen) {
                        MainNavBar(
                            selectedNavItem = uiState.selectedNavItem,
                            onNavBarItemClick = { navItem ->
                                onEvent(MainEvent.OnNavItemClick(navItem))
                            }
                        )
                    }
                },
                snackbarHost = {
                    SnackbarHost(snackbarHostState) { snackbarData ->
                        Snackbar(
                            snackbarData = snackbarData,
                            modifier = Modifier.let {
                                if (compactScreen) {
                                    it
                                } else {
                                    it.offset(
                                        y = -WindowInsets.navigationBars.asPaddingValues()
                                            .calculateBottomPadding()
                                    )
                                }
                            }
                        )
                    }
                },
                floatingActionButton = {
                    if (compactScreen && uiState.selectedNavItem == MainNavItems.ACTIVITIES) {
                        AddFab(
                            onClick = {
                                onAddActivityClick()
                            }
                        )
                    }
                },
                contentWindowInsets = WindowInsets(0.dp)
            ) { innerPadding ->
                val navRailPadding = if (compactScreen) 0.dp else 80.dp

                when (uiState.selectedNavItem) {
                    MainNavItems.HOME -> {
                        homeScreen(
                            Modifier
                                .padding(innerPadding)
                                .padding(start = navRailPadding)
                        )
                    }

                    MainNavItems.ACTIVITIES -> {
                        activitiesScreen(
                            Modifier
                                .padding(innerPadding)
                                .padding(start = navRailPadding)
                        )
                    }

                    MainNavItems.DEALS -> {
                        dealsScreen(
                            Modifier
                                .padding(innerPadding)
                                .padding(start = navRailPadding)
                        )
                    }
                }

                if (uiState.isYearPickerDialogVisible) {
                    YearPickerDialog(
                        onDismissRequest = {
                            onEvent(MainEvent.OnDismissSelectYear)
                        },
                        onYearSelected = { year ->
                            onEvent(MainEvent.OnDismissSelectYear)
                            onSharedEvent(SharedEvent.OnSelectedYearChange(year.toString()))
                        },
                        currentYear = sharedUiState.selectedYear.toInt()
                    )
                }
            }

            if (!compactScreen) {
                MainNavRail(
                    selectedNavItem = uiState.selectedNavItem,
                    onNavRailItemClick = { navItem ->
                        onEvent(MainEvent.OnNavItemClick(navItem))
                    },
                    onMenuClick = {
                        onEvent(MainEvent.OnMenuClick)
                    },
                    onAddActivityClick = {
                        onAddActivityClick()
                    }
                )
                LeftGradient(
                    modifier = Modifier
                        .padding(start = 80.dp)
                        .fillMaxHeight()
                        .width(16.dp)
                        .zIndex(1f)
                )
            }
        }
    } else {
        Scaffold { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Preview
@Composable
private fun MainScreenCompactPreview() {
    SpendlyTheme {
        MainScreen(
            sharedUiState = SharedUiState(
                selectedWallet = Wallet(
                    title = "Dana",
                    currency = "Rp",
                    balance = 1500000,
                    walletId = ""
                )
            ),
            uiState = MainUiState(),
            onSharedEvent = {},
            onEvent = {},
            homeScreen = {},
            activitiesScreen = {},
            dealsScreen = {},
            onAddActivityClick = {}
        )
    }
}

@Preview(device = "spec:width=1280dp,height=800dp,dpi=240")
@Composable
private fun MainScreenMediumPreview() {
    SpendlyTheme {
        MainScreen(
            sharedUiState = SharedUiState(
                selectedWallet = Wallet(
                    title = "Dana",
                    currency = "Rp",
                    balance = 1500000,
                    walletId = ""
                )
            ),
            uiState = MainUiState(),
            onSharedEvent = {},
            onEvent = {},
            homeScreen = {},
            activitiesScreen = {},
            dealsScreen = {},
            onAddActivityClick = {},
            compactScreen = false,
        )
    }
}