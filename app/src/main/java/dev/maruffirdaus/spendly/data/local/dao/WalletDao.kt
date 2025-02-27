package dev.maruffirdaus.spendly.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dev.maruffirdaus.spendly.data.local.model.WalletEntity

@Dao
interface WalletDao {
    @Upsert
    suspend fun upsert(wallet: WalletEntity)

    @Query("SELECT * " +
            "FROM wallets " +
            "WHERE isDeletePending = false " +
            "ORDER BY title ASC")
    suspend fun getWallets(): List<WalletEntity>

    @Query("SELECT * " +
            "FROM wallets " +
            "WHERE isSyncPending = false")
    suspend fun getSyncedWallets(): List<WalletEntity>

    @Query("SELECT * " +
            "FROM wallets " +
            "WHERE isSyncPending = true")
    suspend fun getSyncPendingWallets(): List<WalletEntity>

    @Query("SELECT * " +
            "FROM wallets " +
            "WHERE walletId = :walletId")
    suspend fun getWallet(walletId: String): WalletEntity

    @Query(
        "UPDATE wallets " +
                "SET isSyncPending = true"
    )
    suspend fun markAllAsSyncPending()

    @Delete
    suspend fun delete(wallet: WalletEntity)
}