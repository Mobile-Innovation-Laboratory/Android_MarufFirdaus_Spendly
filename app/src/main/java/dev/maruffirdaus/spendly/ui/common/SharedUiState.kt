package dev.maruffirdaus.spendly.ui.common

import dev.maruffirdaus.spendly.common.model.Wallet
import java.time.LocalDateTime

data class SharedUiState(
    val selectedWallet: Wallet? = null,
    val selectedYear: String = LocalDateTime.now().year.toString(),
    val snackbarMessage: String? = null
)