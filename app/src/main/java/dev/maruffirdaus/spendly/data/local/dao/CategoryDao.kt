package dev.maruffirdaus.spendly.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dev.maruffirdaus.spendly.data.local.model.CategoryEntity

@Dao
interface CategoryDao {
    @Upsert
    suspend fun upsert(category: CategoryEntity)

    @Query(
        "SELECT * " +
                "FROM categories " +
                "WHERE isDeletePending = false " +
                "ORDER BY title ASC"
    )
    suspend fun getCategories(): List<CategoryEntity>

    @Query(
        "SELECT * " +
                "FROM categories " +
                "WHERE isSyncPending = false"
    )
    suspend fun getSyncedCategories(): List<CategoryEntity>

    @Query(
        "SELECT * " +
                "FROM categories " +
                "WHERE isSyncPending = true"
    )
    suspend fun getSyncPendingCategories(): List<CategoryEntity>

    @Query(
        "UPDATE categories " +
                "SET isSyncPending = true"
    )
    suspend fun markAllAsSyncPending()

    @Delete
    suspend fun delete(category: CategoryEntity)
}