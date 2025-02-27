package dev.maruffirdaus.spendly.common.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Activity @OptIn(ExperimentalUuidApi::class) constructor(
    val walletId: String,
    val title: String,
    val date: String,
    val amount: Long,
    val activityId: String = Uuid.random().toString(),
    val categoryId: String? = null
)