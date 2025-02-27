package dev.maruffirdaus.spendly.domain.repository

interface FirebaseCategoryRepository {
    suspend fun syncCategories(userId: String)
}