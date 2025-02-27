package dev.maruffirdaus.spendly.domain.usecase.wallet

import dev.maruffirdaus.spendly.domain.repository.FirebaseWalletRepository
import javax.inject.Inject

class SyncWalletsUseCase @Inject constructor(
    private val repository: FirebaseWalletRepository
) {
    suspend operator fun invoke(userId: String) = repository.syncWallets(userId)
}