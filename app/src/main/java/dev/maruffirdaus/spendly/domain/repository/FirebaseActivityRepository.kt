package dev.maruffirdaus.spendly.domain.repository

interface FirebaseActivityRepository {
    suspend fun syncActivities(userId: String)
}