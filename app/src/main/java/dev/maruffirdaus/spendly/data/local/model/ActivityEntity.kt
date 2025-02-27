package dev.maruffirdaus.spendly.data.local.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(
    tableName = "activities",
    indices = [
        Index(value = ["walletId"]),
        Index(value = ["categoryId"])
    ],
    foreignKeys = [
        ForeignKey(
            entity = WalletEntity::class,
            parentColumns = ["walletId"],
            childColumns = ["walletId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = CategoryEntity::class,
            parentColumns = ["categoryId"],
            childColumns = ["categoryId"],
            onDelete = ForeignKey.SET_NULL
        )
    ]
)
data class ActivityEntity @OptIn(ExperimentalUuidApi::class) constructor(
    val walletId: String,
    val title: String,
    val date: String,
    val amount: Long,
    @PrimaryKey val activityId: String = Uuid.random().toString(),
    val categoryId: String? = null,
    val isSyncPending: Boolean = true,
    val isDeletePending: Boolean = false
)