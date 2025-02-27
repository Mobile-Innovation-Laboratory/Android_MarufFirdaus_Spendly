package dev.maruffirdaus.spendly.common.model

data class Deal(
    val title: String,
    val url: String,
    val imageUrl: String,
    val cut: Int,
    val currency: String,
    val regularPrice: Number,
    val dealPrice: Number
)