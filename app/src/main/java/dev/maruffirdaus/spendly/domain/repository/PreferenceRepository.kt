package dev.maruffirdaus.spendly.domain.repository

interface PreferenceRepository {
    suspend fun saveSelectedWalletId(walletId: String?)

    suspend fun getSelectedWalletId(): String?

    suspend fun saveDataSyncEnabled(isEnabled: Boolean)

    suspend fun getDataSyncEnabled(): Boolean
}