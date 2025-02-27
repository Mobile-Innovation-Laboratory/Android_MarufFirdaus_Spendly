package dev.maruffirdaus.spendly.domain.repository

import dev.maruffirdaus.spendly.common.model.Activity

interface ActivityRepository {
    suspend fun addEditActivity(activity: Activity)

    suspend fun getActivities(
        walletId: String,
        categoryId: String?,
        year: String,
        month: String?
    ): List<Activity>

    suspend fun getActivity(activityId: String): Activity?

    suspend fun markAllAsSyncPending()

    suspend fun deleteActivity(activity: Activity, isDataSyncEnabled: Boolean)
}