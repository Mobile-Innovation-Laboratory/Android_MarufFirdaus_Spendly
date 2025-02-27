package dev.maruffirdaus.spendly.data.repository

import dev.maruffirdaus.spendly.data.local.dao.WalletDao
import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.data.util.toWallet
import dev.maruffirdaus.spendly.data.util.toWalletEntity
import dev.maruffirdaus.spendly.domain.repository.WalletRepository
import javax.inject.Inject

class WalletRepositoryImpl @Inject constructor(
    private val dao: WalletDao
) : WalletRepository {
    override suspend fun addEditWallet(wallet: Wallet) = dao.upsert(wallet.toWalletEntity())

    override suspend fun getWallets(): List<Wallet> = dao.getWallets().map { it.toWallet() }

    override suspend fun getWallet(walletId: String): Wallet = dao.getWallet(walletId).toWallet()

    override suspend fun markAllAsSyncPending() = dao.markAllAsSyncPending()

    override suspend fun deleteWallet(wallet: Wallet, isDataSyncEnabled: Boolean) =
        if (isDataSyncEnabled) {
            dao.upsert(wallet.toWalletEntity().copy(isDeletePending = true))
        } else {
            dao.delete(wallet.toWalletEntity())
        }
}