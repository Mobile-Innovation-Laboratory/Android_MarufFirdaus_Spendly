package dev.maruffirdaus.spendly.common.model

import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

data class Category @OptIn(ExperimentalUuidApi::class) constructor(
    val title: String,
    val categoryId: String = Uuid.random().toString()
)