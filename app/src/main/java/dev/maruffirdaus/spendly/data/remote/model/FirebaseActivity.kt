package dev.maruffirdaus.spendly.data.remote.model

data class FirebaseActivity(
    val walletId: String = "",
    val categoryId: String? = null,
    val activityId: String = "",
    val title: String = "",
    val date: String = "",
    val amount: Long = 0,
    val syncPending: Boolean = true,
    val deletePending: Boolean = false
)
