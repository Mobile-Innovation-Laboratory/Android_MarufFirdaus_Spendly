package dev.maruffirdaus.spendly.data.util

import dev.maruffirdaus.spendly.data.local.model.CategoryEntity
import dev.maruffirdaus.spendly.data.remote.model.FirebaseCategory

fun CategoryEntity.toFirebaseCategory(): FirebaseCategory = FirebaseCategory(
    categoryId = this.categoryId,
    title = this.title,
    syncPending = this.isSyncPending,
    deletePending = this.isDeletePending
)