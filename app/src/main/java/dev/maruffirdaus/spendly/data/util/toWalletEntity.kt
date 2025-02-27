package dev.maruffirdaus.spendly.data.util

import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.data.local.model.WalletEntity
import dev.maruffirdaus.spendly.data.remote.model.FirebaseWallet

fun Wallet.toWalletEntity(): WalletEntity = WalletEntity(
    title = this.title,
    currency = this.currency,
    balance = this.balance,
    walletId = this.walletId
)

fun FirebaseWallet.toWalletEntity(): WalletEntity = WalletEntity(
    title = this.title,
    currency = this.currency,
    balance = this.balance,
    walletId = this.walletId,
    isSyncPending = this.syncPending,
    isDeletePending = this.deletePending
)