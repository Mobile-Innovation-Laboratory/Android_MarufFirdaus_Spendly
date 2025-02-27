package dev.maruffirdaus.spendly.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObjects
import dev.maruffirdaus.spendly.data.local.dao.ActivityDao
import dev.maruffirdaus.spendly.data.remote.model.FirebaseActivity
import dev.maruffirdaus.spendly.data.util.toActivityEntity
import dev.maruffirdaus.spendly.data.util.toFirebaseActivity
import dev.maruffirdaus.spendly.domain.repository.FirebaseActivityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseActivityRepositoryImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val dao: ActivityDao
) : FirebaseActivityRepository {
    override suspend fun syncActivities(userId: String) {
        runCatching {
            val syncPendingActivities = dao.getSyncPendingActivities()
            val (activitiesToDeleteRemotely, activitiesToUpsertRemotely) =
                syncPendingActivities.partition { it.isDeletePending }

            activitiesToUpsertRemotely.forEach { activity ->
                firestore.collection("users")
                    .document(userId)
                    .collection("activities")
                    .document(activity.activityId)
                    .set(activity.copy(isSyncPending = false).toFirebaseActivity())
                    .addOnSuccessListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            dao.upsert(activity.copy(isSyncPending = false))
                        }
                    }
            }

            activitiesToDeleteRemotely.forEach { activity ->
                firestore.collection("users")
                    .document(userId)
                    .collection("activities")
                    .document(activity.activityId)
                    .delete()
                    .addOnSuccessListener {
                        CoroutineScope(Dispatchers.IO).launch {
                            dao.delete(activity)
                        }
                    }
            }

            val remoteActivities = firestore.collection("users")
                .document(userId)
                .collection("activities")
                .get()
                .await()
                .toObjects<FirebaseActivity>()

            remoteActivities.forEach { activity ->
                dao.upsert(activity.toActivityEntity())
            }

            val localActivities = dao.getSyncedActivities()
            val remoteIds = remoteActivities.map { it.activityId }.toSet()
            val activitiesToDeleteLocally = localActivities.filter { it.activityId !in remoteIds }

            activitiesToDeleteLocally.forEach { activity ->
                dao.delete(activity)
            }
        }
    }
}