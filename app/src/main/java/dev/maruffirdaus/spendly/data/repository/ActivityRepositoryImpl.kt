package dev.maruffirdaus.spendly.data.repository

import dev.maruffirdaus.spendly.data.local.dao.ActivityDao
import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.data.util.toActivity
import dev.maruffirdaus.spendly.data.util.toActivityEntity
import dev.maruffirdaus.spendly.domain.repository.ActivityRepository
import javax.inject.Inject

class ActivityRepositoryImpl @Inject constructor(
    private val dao: ActivityDao
) : ActivityRepository {
    override suspend fun addEditActivity(activity: Activity) =
        dao.upsert(activity.toActivityEntity())

    override suspend fun getActivities(
        walletId: String,
        categoryId: String?,
        year: String,
        month: String?
    ): List<Activity> {
        val date = buildString {
            append(year)
            if (month != null) {
                append("-")
                append(month)
            }
            append("%")
        }

        return if (categoryId != null) {
            dao.getActivities(walletId, categoryId, date).map { it.toActivity() }
        } else {
            dao.getActivities(walletId, date).map { it.toActivity() }
        }
    }

    override suspend fun getActivity(activityId: String): Activity? =
        dao.getActivity(activityId)?.toActivity()

    override suspend fun markAllAsSyncPending() = dao.markAllAsSyncPending()

    override suspend fun deleteActivity(activity: Activity, isDataSyncEnabled: Boolean) =
        if (isDataSyncEnabled) {
            dao.upsert(activity.toActivityEntity().copy(isDeletePending = true))
        } else {
            dao.delete(activity.toActivityEntity())
        }
}