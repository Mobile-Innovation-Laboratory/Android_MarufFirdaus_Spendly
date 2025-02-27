package dev.maruffirdaus.spendly.domain.repository

import dev.maruffirdaus.spendly.common.model.User

interface AuthRepository {
    suspend fun signUp(email: String, password: String): Result<Unit>

    suspend fun signIn(email: String, password: String): Result<Unit>

    fun signOut(): Result<Unit>

    fun getUser(): User?
}