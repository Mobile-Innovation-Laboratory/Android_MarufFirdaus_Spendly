package dev.maruffirdaus.spendly.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@Entity(tableName = "categories")
data class CategoryEntity @OptIn(ExperimentalUuidApi::class) constructor(
    val title: String,
    @PrimaryKey val categoryId: String = Uuid.random().toString(),
    val isSyncPending: Boolean = true,
    val isDeletePending: Boolean = false
)