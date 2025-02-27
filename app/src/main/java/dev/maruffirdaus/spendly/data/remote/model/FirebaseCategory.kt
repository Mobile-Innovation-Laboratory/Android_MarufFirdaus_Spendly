package dev.maruffirdaus.spendly.data.remote.model

data class FirebaseCategory(
    val categoryId: String = "",
    val title: String = "",
    val syncPending: Boolean = true,
    val deletePending: Boolean = false
)