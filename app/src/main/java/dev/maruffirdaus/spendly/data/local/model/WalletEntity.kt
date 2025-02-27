package dev.maruffirdaus.spendly.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "wallets")
data class WalletEntity @OptIn(ExperimentalUuidApi::class) constructor(
    val title: String,
    val currency: String,
    val balance: Long,
    @PrimaryKey val walletId: String = Uuid.random().toString(),
    val isSyncPending: Boolean = true,
    val isDeletePending: Boolean = false
)