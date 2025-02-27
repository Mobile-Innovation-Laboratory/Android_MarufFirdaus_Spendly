package dev.maruffirdaus.spendly.domain.usecase.activity

import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.domain.repository.ActivityRepository
import javax.inject.Inject

class GetActivityUseCase @Inject constructor(
    private val repository: ActivityRepository
) {
    suspend operator fun invoke(activityId: String): Activity? = repository.getActivity(activityId)
}