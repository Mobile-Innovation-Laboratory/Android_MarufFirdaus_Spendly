package dev.maruffirdaus.spendly.domain.usecase.activity

import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.domain.repository.ActivityRepository
import javax.inject.Inject

class GetActivitiesUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(
        walletId: String,
        categoryId: String?,
        year: String,
        month: String?
    ): List<Activity> = repository.getActivities(walletId, categoryId, year, month)
}