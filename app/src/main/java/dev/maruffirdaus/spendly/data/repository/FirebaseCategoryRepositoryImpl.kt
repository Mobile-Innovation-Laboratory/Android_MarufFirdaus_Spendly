package dev.maruffirdaus.spendly.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import dev.maruffirdaus.spendly.data.local.dao.CategoryDao
import dev.maruffirdaus.spendly.data.remote.model.FirebaseCategory
import dev.maruffirdaus.spendly.data.util.toCategoryEntity
import dev.maruffirdaus.spendly.data.util.toFirebaseCategory
import dev.maruffirdaus.spendly.domain.repository.FirebaseCategoryRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseCategoryRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dao: CategoryDao
) : FirebaseCategoryRepository {
    override suspend fun syncCategories(userId: String) {
        runCatching {
            val syncPendingCategories = dao.getSyncPendingCategories()
            val (categoriesToDeleteRemotely, categoriesToUpsertRemotely) =
                syncPendingCategories.partition { it.isDeletePending }

            categoriesToUpsertRemotely.forEach { category ->
                firestore.collection("users")
                    .document(userId)
                    .collection("categories")
                    .document(category.categoryId)
                    .set(category.copy(isSyncPending = false).toFirebaseCategory())
                    .addOnSuccessListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            dao.upsert(category.copy(isSyncPending = false))
                        }
                    }
            }

            categoriesToDeleteRemotely.forEach { category ->
                firestore.collection("users")
                    .document(userId)
                    .collection("categories")
                    .document(category.categoryId)
                    .delete()
                    .addOnSuccessListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            dao.delete(category)
                        }
                    }
            }

            val remoteCategories = firestore.collection("users")
                .document(userId)
                .collection("categories")
                .get()
                .await()
                .toObjects<FirebaseCategory>()

            remoteCategories.forEach { category ->
                dao.upsert(category.toCategoryEntity())
            }

            val localCategories = dao.getSyncedCategories()
            val remoteIds = remoteCategories.map { it.categoryId }.toSet()
            val categoriesToDeleteLocally = localCategories.filter { it.categoryId !in remoteIds }

            categoriesToDeleteLocally.forEach { category ->
                dao.delete(category)
            }
        }
    }
}