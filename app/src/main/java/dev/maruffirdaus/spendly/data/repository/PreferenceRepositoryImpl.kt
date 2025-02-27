package dev.maruffirdaus.spendly.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import dev.maruffirdaus.spendly.domain.repository.PreferenceRepository
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class PreferenceRepositoryImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : PreferenceRepository {
    override suspend fun saveSelectedWalletId(walletId: String?) {
        dataStore.edit {
            if (walletId == null) it.remove(SELECTED_WALLET) else it[SELECTED_WALLET] = walletId
        }
    }

    override suspend fun getSelectedWalletId(): String? = dataStore.data.map {
        it[SELECTED_WALLET]
    }.firstOrNull()

    override suspend fun saveDataSyncEnabled(isEnabled: Boolean) {
        dataStore.edit {
            it[DATA_SYNC_ENABLED] = isEnabled
        }
    }

    override suspend fun getDataSyncEnabled(): Boolean = dataStore.data.map {
        it[DATA_SYNC_ENABLED]
    }.firstOrNull() ?: false

    companion object {
        private val SELECTED_WALLET = stringPreferencesKey("selected_wallet")
        private val DATA_SYNC_ENABLED = booleanPreferencesKey("data_sync_enabled")
    }
}