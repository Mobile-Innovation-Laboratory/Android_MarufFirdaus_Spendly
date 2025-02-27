package dev.maruffirdaus.spendly.domain.repository

import dev.maruffirdaus.spendly.common.model.Wallet

interface WalletRepository {
    suspend fun addEditWallet(wallet: Wallet)

    suspend fun getWallets(): List<Wallet>

    suspend fun getWallet(walletId: String): Wallet

    suspend fun markAllAsSyncPending()

    suspend fun deleteWallet(wallet: Wallet, isDataSyncEnabled: Boolean)
}