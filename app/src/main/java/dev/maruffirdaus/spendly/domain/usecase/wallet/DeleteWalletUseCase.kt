package dev.maruffirdaus.spendly.domain.usecase.wallet

import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.domain.repository.WalletRepository
import javax.inject.Inject

class DeleteWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(wallet: Wallet, isDataSyncEnabled: Boolean) =
        repository.deleteWallet(wallet, isDataSyncEnabled)
}