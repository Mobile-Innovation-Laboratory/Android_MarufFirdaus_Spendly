package dev.maruffirdaus.spendly.domain.usecase.auth

import dev.maruffirdaus.spendly.domain.repository.AuthRepository
import javax.inject.Inject

class SignUpUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(email: String, password: String): Result<Unit> =
        repository.signUp(email, password)
}