package dev.maruffirdaus.spendly.domain.usecase.activity

import dev.maruffirdaus.spendly.domain.repository.FirebaseActivityRepository
import javax.inject.Inject

class SyncActivitiesUseCase @Inject constructor(
    private val repository: FirebaseActivityRepository
) {
    suspend operator fun invoke(userId: String) = repository.syncActivities(userId)
}