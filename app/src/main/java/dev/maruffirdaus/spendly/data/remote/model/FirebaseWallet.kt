package dev.maruffirdaus.spendly.data.remote.model

data class FirebaseWallet(
    val walletId: String = "",
    val title: String = "",
    val currency: String = "",
    val balance: Long = 0,
    val syncPending: Boolean = true,
    val deletePending: Boolean = false
)