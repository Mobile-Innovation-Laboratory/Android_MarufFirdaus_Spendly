package dev.maruffirdaus.spendly.domain.usecase.activity

import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.domain.repository.ActivityRepository
import dev.maruffirdaus.spendly.domain.repository.WalletRepository
import javax.inject.Inject

class AddEditActivityUseCase @Inject constructor(
    private val walletRepository: WalletRepository,
    private val activityRepository: ActivityRepository
) {
    suspend operator fun invoke(activity: Activity) {
        val wallet = walletRepository.getWallet(activity.walletId)
        val oldActivity = activityRepository.getActivity(activity.activityId)

        walletRepository.addEditWallet(
            wallet.copy(
                balance = wallet.balance + ((oldActivity?.amount ?: 0) * -1) + activity.amount
            )
        )
        activityRepository.addEditActivity(activity)
    }
}