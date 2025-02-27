package dev.maruffirdaus.spendly.data.util

import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.data.local.model.ActivityEntity
import dev.maruffirdaus.spendly.data.remote.model.FirebaseActivity

fun Activity.toActivityEntity(): ActivityEntity = ActivityEntity(
    walletId = this.walletId,
    title = this.title,
    date = this.date,
    amount = this.amount,
    activityId = this.activityId,
    categoryId = this.categoryId
)

fun FirebaseActivity.toActivityEntity(): ActivityEntity = ActivityEntity(
    walletId = this.walletId,
    title = this.title,
    date = this.date,
    amount = this.amount,
    activityId = this.activityId,
    categoryId = this.categoryId,
    isSyncPending = this.syncPending,
    isDeletePending = this.deletePending
)