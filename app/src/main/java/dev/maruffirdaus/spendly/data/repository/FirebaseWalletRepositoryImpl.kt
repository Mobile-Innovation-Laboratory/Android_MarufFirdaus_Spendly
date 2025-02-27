package dev.maruffirdaus.spendly.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import dev.maruffirdaus.spendly.data.local.dao.WalletDao
import dev.maruffirdaus.spendly.data.remote.model.FirebaseWallet
import dev.maruffirdaus.spendly.data.util.toFirebaseWallet
import dev.maruffirdaus.spendly.data.util.toWalletEntity
import dev.maruffirdaus.spendly.domain.repository.FirebaseWalletRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseWalletRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dao: WalletDao
) : FirebaseWalletRepository {
    override suspend fun syncWallets(userId: String) {
        runCatching {
            val syncPendingWallets = dao.getSyncPendingWallets()
            val (walletsToDeleteRemotely, walletsToUpsertRemotely) =
                syncPendingWallets.partition { it.isDeletePending }

            walletsToUpsertRemotely.forEach { wallet ->
                firestore.collection("users")
                    .document(userId)
                    .collection("wallets")
                    .document(wallet.walletId)
                    .set(wallet.copy(isSyncPending = false).toFirebaseWallet())
                    .addOnSuccessListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            dao.upsert(wallet.copy(isSyncPending = false))
                        }
                    }
            }

            walletsToDeleteRemotely.forEach { wallet ->
                firestore.collection("users")
                    .document(userId)
                    .collection("wallets")
                    .document(wallet.walletId)
                    .delete()
                    .addOnSuccessListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            dao.delete(wallet)
                        }
                    }
            }

            val remoteWallets = firestore.collection("users")
                .document(userId)
                .collection("wallets")
                .get()
                .await()
                .toObjects<FirebaseWallet>()

            remoteWallets.forEach { wallet ->
                dao.upsert(wallet.toWalletEntity())
            }

            val localWallets = dao.getSyncedWallets()
            val remoteIds = remoteWallets.map { it.walletId }.toSet()
            val walletsToDeleteLocally = localWallets.filter { it.walletId !in remoteIds }

            walletsToDeleteLocally.forEach { wallet ->
                dao.delete(wallet)
            }
        }
    }
}