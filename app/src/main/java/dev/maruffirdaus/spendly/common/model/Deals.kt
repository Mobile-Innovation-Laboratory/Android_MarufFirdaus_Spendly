package dev.maruffirdaus.spendly.common.model

data class Deals(
    val nextOffset: Int = 0,
    val hasMore: Boolean = false,
    val items: List<Deal> = emptyList()
)