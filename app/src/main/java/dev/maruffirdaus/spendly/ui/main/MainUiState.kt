package dev.maruffirdaus.spendly.ui.main

import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.ui.main.constant.MainNavItems

data class MainUiState(
    val selectedNavItem: MainNavItems = MainNavItems.HOME,
    val wallets: List<Wallet> = emptyList(),
    val isMenuVisible: Boolean = false,
    val isWalletsLoading: Boolean = false,
    val isDeleteWalletDialogVisible: Boolean = false,
    val isYearPickerDialogVisible: Boolean = false,
)