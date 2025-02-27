package dev.maruffirdaus.spendly.common.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Wallet @OptIn(ExperimentalUuidApi::class) constructor(
    val title: String,
    val currency: String,
    val balance: Long,
    val walletId: String = Uuid.random().toString()
)