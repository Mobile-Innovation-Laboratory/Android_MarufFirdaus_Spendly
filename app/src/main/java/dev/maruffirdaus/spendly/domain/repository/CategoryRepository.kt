package dev.maruffirdaus.spendly.domain.repository

import dev.maruffirdaus.spendly.common.model.Category

interface CategoryRepository {
    suspend fun addEditCategory(category: Category)

    suspend fun getCategories(): List<Category>

    suspend fun markAllAsSyncPending()

    suspend fun deleteCategory(category: Category, isDataSyncEnabled: Boolean)
}