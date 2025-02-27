package dev.maruffirdaus.spendly.data.util

import dev.maruffirdaus.spendly.data.local.model.ActivityEntity
import dev.maruffirdaus.spendly.data.remote.model.FirebaseActivity

fun ActivityEntity.toFirebaseActivity(): FirebaseActivity = FirebaseActivity(
    walletId = this.walletId,
    categoryId = this.categoryId,
    activityId = this.activityId,
    title = this.title,
    date = this.date,
    amount = this.amount,
    syncPending = this.isSyncPending,
    deletePending = this.isDeletePending
)