package dev.maruffirdaus.spendly.data.util

import dev.maruffirdaus.spendly.common.model.Category
import dev.maruffirdaus.spendly.data.local.model.CategoryEntity
import dev.maruffirdaus.spendly.data.remote.model.FirebaseCategory

fun Category.toCategoryEntity(): CategoryEntity = CategoryEntity(
    title = this.title,
    categoryId = this.categoryId
)

fun FirebaseCategory.toCategoryEntity(): CategoryEntity = CategoryEntity(
    title = this.title,
    categoryId = this.categoryId,
    isSyncPending = this.syncPending,
    isDeletePending = this.deletePending
)