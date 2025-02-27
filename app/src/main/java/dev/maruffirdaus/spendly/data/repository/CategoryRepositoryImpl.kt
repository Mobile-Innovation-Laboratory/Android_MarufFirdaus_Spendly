package dev.maruffirdaus.spendly.data.repository

import dev.maruffirdaus.spendly.data.local.dao.CategoryDao
import dev.maruffirdaus.spendly.common.model.Category
import dev.maruffirdaus.spendly.data.util.toCategory
import dev.maruffirdaus.spendly.data.util.toCategoryEntity
import dev.maruffirdaus.spendly.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val dao: CategoryDao
) : CategoryRepository {
    override suspend fun addEditCategory(category: Category) =
        dao.upsert(category.toCategoryEntity())

    override suspend fun getCategories(): List<Category> =
        dao.getCategories().map { it.toCategory() }

    override suspend fun markAllAsSyncPending() = dao.markAllAsSyncPending()

    override suspend fun deleteCategory(category: Category, isDataSyncEnabled: Boolean) =
        if (isDataSyncEnabled) {
            dao.upsert(category.toCategoryEntity().copy(isDeletePending = true))
        } else {
            dao.delete(category.toCategoryEntity())
        }
}