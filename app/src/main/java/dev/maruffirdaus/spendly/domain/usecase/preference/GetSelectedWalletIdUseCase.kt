package dev.maruffirdaus.spendly.domain.usecase.preference

import dev.maruffirdaus.spendly.domain.repository.PreferenceRepository
import javax.inject.Inject

class GetSelectedWalletIdUseCase @Inject constructor(
    private val repository: PreferenceRepository
) {
    suspend operator fun invoke(): String? = repository.getSelectedWalletId()
}