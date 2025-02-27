package dev.maruffirdaus.spendly.ui.home

import dev.maruffirdaus.spendly.common.model.Wallet

data class HomeUiState(
    val scrollPosition: Int = 0,
    val walletToDelete: Wallet? = null,
    val incomeData: List<Long> = emptyList(),
    val expenseData: List<Long> = emptyList(),
    val isLoading: Boolean = false,
    val isDeleteWalletDialogVisible: Boolean = false
)