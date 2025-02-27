package dev.maruffirdaus.spendly.domain.usecase.preference

import dev.maruffirdaus.spendly.domain.repository.PreferenceRepository
import javax.inject.Inject

class GetDataSyncEnabledUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {
    suspend operator fun invoke(): Boolean = repository.getDataSyncEnabled()
}