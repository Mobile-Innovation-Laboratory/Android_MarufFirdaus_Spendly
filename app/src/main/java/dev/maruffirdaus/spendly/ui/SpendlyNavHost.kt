package dev.maruffirdaus.spendly.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import dev.maruffirdaus.spendly.ui.activities.ActivitiesScreen
import dev.maruffirdaus.spendly.ui.activities.ActivitiesViewModel
import dev.maruffirdaus.spendly.ui.addeditactivity.AddEditActivityScreen
import dev.maruffirdaus.spendly.ui.addeditactivity.AddEditActivityViewModel
import dev.maruffirdaus.spendly.ui.addeditwallet.AddEditWalletScreen
import dev.maruffirdaus.spendly.ui.addeditwallet.AddEditWalletViewModel
import dev.maruffirdaus.spendly.ui.common.SharedViewModel
import dev.maruffirdaus.spendly.ui.deals.DealsScreen
import dev.maruffirdaus.spendly.ui.deals.DealsViewModel
import dev.maruffirdaus.spendly.ui.home.HomeScreen
import dev.maruffirdaus.spendly.ui.home.HomeViewModel
import dev.maruffirdaus.spendly.ui.main.MainEvent
import dev.maruffirdaus.spendly.ui.main.MainScreen
import dev.maruffirdaus.spendly.ui.main.MainViewModel
import dev.maruffirdaus.spendly.ui.settings.SettingsScreen
import dev.maruffirdaus.spendly.ui.settings.SettingsViewModel
import dev.maruffirdaus.spendly.ui.signin.SignInScreen
import dev.maruffirdaus.spendly.ui.signin.SignInViewModel
import dev.maruffirdaus.spendly.ui.signup.SignUpScreen
import dev.maruffirdaus.spendly.ui.signup.SignUpViewModel
import dev.maruffirdaus.spendly.ui.welcome.WelcomeScreen
import dev.maruffirdaus.spendly.ui.welcome.WelcomeViewModel
import kotlinx.serialization.Serializable

@Serializable
object Welcome

@Serializable
data class SignIn(
    val shouldEnableDataSync: Boolean,
    val shouldNavigateToMainScreen: Boolean,
)

@Serializable
object SignUp

@Serializable
object Main

@Serializable
data class AddEditWallet(
    val walletId: String? = null
)

@Serializable
data class AddEditActivity(
    val walletId: String,
    val currency: String,
    val activityId: String? = null
)

@Serializable
object Settings

@Composable
fun SpendlyNavHost(
    country: String = "US",
    compactScreen: Boolean = true
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Main
    ) {
        composable<Welcome> {
            val viewModel: WelcomeViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            WelcomeScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }

        composable<SignIn> {
            val args = it.toRoute<SignIn>()
            val viewModel: SignInViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            SignInScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                shouldEnableDataSync = args.shouldEnableDataSync,
                shouldNavigateToMainScreen = args.shouldNavigateToMainScreen,
                navController = navController
            )
        }

        composable<SignUp> {
            val viewModel: SignUpViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            SignUpScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }

        composable<Main> {
            val sharedViewModel: SharedViewModel = hiltViewModel()
            val mainViewModel: MainViewModel = hiltViewModel()
            val homeViewModel: HomeViewModel = hiltViewModel()
            val activitiesViewModel: ActivitiesViewModel = hiltViewModel()
            val dealsViewModel: DealsViewModel = hiltViewModel()

            val sharedUiState by sharedViewModel.uiState.collectAsState()
            val mainUiState by mainViewModel.uiState.collectAsState()
            val homeUiState by homeViewModel.uiState.collectAsState()
            val activitiesUiState by activitiesViewModel.uiState.collectAsState()
            val dealsUiState by dealsViewModel.uiState.collectAsState()

            MainScreen(
                sharedUiState = sharedUiState,
                uiState = mainUiState,
                onSharedEvent = sharedViewModel::onEvent,
                onEvent = mainViewModel::onEvent,
                homeScreen = { modifier ->
                    var hasAnotherWallet by remember { mutableStateOf(false) }

                    LaunchedEffect(mainUiState.wallets) {
                        hasAnotherWallet = 1 < mainUiState.wallets.size
                    }

                    HomeScreen(
                        hasAnotherWallet = hasAnotherWallet,
                        sharedUiState = sharedUiState,
                        uiState = homeUiState,
                        onSharedEvent = sharedViewModel::onEvent,
                        onEvent = homeViewModel::onEvent,
                        onMenuClick = if (compactScreen) {
                            {
                                mainViewModel.onEvent(MainEvent.OnMenuClick)
                            }
                        } else {
                            null
                        },
                        onSelectYearClick = {
                            mainViewModel.onEvent(MainEvent.OnSelectYearClick)
                        },
                        onRefreshWallets = {
                            mainViewModel.onEvent(MainEvent.OnRefreshWallets)
                        },
                        modifier = modifier,
                        navController = navController
                    )
                },
                activitiesScreen = { modifier ->
                    ActivitiesScreen(
                        sharedUiState = sharedUiState,
                        uiState = activitiesUiState,
                        onSharedEvent = sharedViewModel::onEvent,
                        onEvent = activitiesViewModel::onEvent,
                        onMenuClick = if (compactScreen) {
                            {
                                mainViewModel.onEvent(MainEvent.OnMenuClick)
                            }
                        } else {
                            null
                        },
                        onSelectYearClick = {
                            mainViewModel.onEvent(MainEvent.OnSelectYearClick)
                        },
                        onRefreshWallets = {
                            mainViewModel.onEvent(MainEvent.OnRefreshWallets)
                        },
                        modifier = modifier,
                        compactScreen = compactScreen,
                        navController = navController
                    )
                },
                dealsScreen = { modifier ->
                    DealsScreen(
                        uiState = dealsUiState,
                        onEvent = dealsViewModel::onEvent,
                        onMenuClick = if (compactScreen) {
                            {
                                mainViewModel.onEvent(MainEvent.OnMenuClick)
                            }
                        } else {
                            null
                        },
                        modifier = modifier,
                        country = country,
                        compactScreen = compactScreen
                    )
                },
                onAddActivityClick = {
                    sharedUiState.selectedWallet?.let { wallet ->
                        navController.navigate(
                            AddEditActivity(
                                walletId = wallet.walletId,
                                currency = wallet.currency
                            )
                        )
                    }
                },
                compactScreen = compactScreen,
                navController = navController
            )
        }

        composable<AddEditWallet> {
            val args = it.toRoute<AddEditWallet>()
            val viewModel: AddEditWalletViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            AddEditWalletScreen(
                uiState = uiState,
                onEvent = viewModel::onEvent,
                walletId = args.walletId,
                navController = navController
            )
        }

        composable<AddEditActivity> {
            val args = it.toRoute<AddEditActivity>()
            val viewModel: AddEditActivityViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            AddEditActivityScreen(
                walletId = args.walletId,
                currency = args.currency,
                uiState = uiState,
                onEvent = viewModel::onEvent,
                activityId = args.activityId,
                navController = navController
            )
        }

        composable<Settings> {
            val viewModel: SettingsViewModel = hiltViewModel()
            val uiState = viewModel.uiState.collectAsState()

            SettingsScreen(
                uiState = uiState.value,
                onEvent = viewModel::onEvent,
                navController = navController
            )
        }
    }
}