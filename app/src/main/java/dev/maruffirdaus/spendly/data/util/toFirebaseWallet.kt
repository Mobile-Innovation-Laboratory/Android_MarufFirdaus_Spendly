package dev.maruffirdaus.spendly.data.util

import dev.maruffirdaus.spendly.data.local.model.WalletEntity
import dev.maruffirdaus.spendly.data.remote.model.FirebaseWallet

fun WalletEntity.toFirebaseWallet(): FirebaseWallet = FirebaseWallet(
    walletId = this.walletId,
    title = this.title,
    currency = this.currency,
    balance = this.balance,
    syncPending = this.isSyncPending,
    deletePending = this.isDeletePending
)