package dev.maruffirdaus.spendly.domain.usecase.activity

import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.domain.repository.ActivityRepository
import dev.maruffirdaus.spendly.domain.repository.WalletRepository
import javax.inject.Inject

class DeleteActivityUseCase @Inject constructor(
    private val walletRepository: WalletRepository,
    private val activityRepository: ActivityRepository
) {
    suspend operator fun invoke(activity: Activity, isDataSyncEnabled: Boolean) {
        val wallet = walletRepository.getWallet(activity.walletId)

        walletRepository.addEditWallet(
            wallet.copy(
                balance = wallet.balance + (activity.amount * -1)
            )
        )
        activityRepository.deleteActivity(activity, isDataSyncEnabled)
    }
}