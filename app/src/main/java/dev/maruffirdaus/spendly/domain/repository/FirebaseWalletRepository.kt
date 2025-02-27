package dev.maruffirdaus.spendly.domain.repository

interface FirebaseWalletRepository {
    suspend fun syncWallets(userId: String)
}