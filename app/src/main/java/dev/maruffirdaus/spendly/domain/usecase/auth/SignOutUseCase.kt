package dev.maruffirdaus.spendly.domain.usecase.auth

import dev.maruffirdaus.spendly.domain.repository.ActivityRepository
import dev.maruffirdaus.spendly.domain.repository.AuthRepository
import dev.maruffirdaus.spendly.domain.repository.CategoryRepository
import dev.maruffirdaus.spendly.domain.repository.WalletRepository
import javax.inject.Inject

class SignOutUseCase @Inject constructor(
    private val authRepository: AuthRepository,
    private val walletRepository: WalletRepository,
    private val categoryRepository: CategoryRepository,
    private val activityRepository: ActivityRepository
) {
    suspend operator fun invoke(): Result<Unit> {
        val result = authRepository.signOut()

        if (result.isSuccess) {
            walletRepository.markAllAsSyncPending()
            categoryRepository.markAllAsSyncPending()
            activityRepository.markAllAsSyncPending()
        }

        return result
    }
}