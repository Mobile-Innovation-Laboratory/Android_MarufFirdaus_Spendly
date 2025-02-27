package dev.maruffirdaus.spendly.domain.usecase.wallet

import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.domain.repository.WalletRepository
import javax.inject.Inject

class GetWalletUseCase @Inject constructor(
    private val repository: WalletRepository
) {
    suspend operator fun invoke(walletId: String): Wallet = repository.getWallet(walletId)
}