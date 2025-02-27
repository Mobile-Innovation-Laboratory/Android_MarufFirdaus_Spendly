package dev.maruffirdaus.spendly.data.util

import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.data.local.model.ActivityEntity

fun ActivityEntity.toActivity(): Activity = Activity(
    walletId = this.walletId,
    title = this.title,
    date = this.date,
    amount = this.amount,
    activityId = this.activityId,
    categoryId = this.categoryId
)