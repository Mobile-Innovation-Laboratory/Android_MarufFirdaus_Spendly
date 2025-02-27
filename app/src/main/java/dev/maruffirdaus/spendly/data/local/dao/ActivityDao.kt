package dev.maruffirdaus.spendly.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import dev.maruffirdaus.spendly.data.local.model.ActivityEntity

@Dao
interface ActivityDao {
    @Upsert
    suspend fun upsert(activity: ActivityEntity)

    @Query(
        "SELECT * " +
                "FROM activities " +
                "WHERE walletId = :walletId AND date LIKE :date AND isDeletePending = false " +
                "ORDER BY date DESC, title ASC"
    )
    suspend fun getActivities(walletId: String, date: String): List<ActivityEntity>

    @Query(
        "SELECT * " +
                "FROM activities " +
                "WHERE walletId = :walletId AND categoryId = :categoryId AND date LIKE :date AND isDeletePending = false " +
                "ORDER BY date DESC, title ASC"
    )
    suspend fun getActivities(
        walletId: String,
        categoryId: String,
        date: String
    ): List<ActivityEntity>

    @Query(
        "SELECT * " +
                "FROM activities " +
                "WHERE isSyncPending = false"
    )
    suspend fun getSyncedActivities(): List<ActivityEntity>

    @Query(
        "SELECT * " +
                "FROM activities " +
                "WHERE isSyncPending = true"
    )
    suspend fun getSyncPendingActivities(): List<ActivityEntity>

    @Query(
        "SELECT * " +
                "FROM activities " +
                "WHERE activityId = :activityId"
    )
    suspend fun getActivity(activityId: String): ActivityEntity?

    @Query(
        "UPDATE activities " +
        "SET isSyncPending = true"
    )
    suspend fun markAllAsSyncPending()

    @Delete
    suspend fun delete(activity: ActivityEntity)
}