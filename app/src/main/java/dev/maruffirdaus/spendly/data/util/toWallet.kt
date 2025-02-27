package dev.maruffirdaus.spendly.data.util

import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.data.local.model.WalletEntity

fun WalletEntity.toWallet(): Wallet = Wallet(
    title = this.title,
    currency = this.currency,
    balance = this.balance,
    walletId = this.walletId
)