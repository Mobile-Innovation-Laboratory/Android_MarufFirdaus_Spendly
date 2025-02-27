package dev.maruffirdaus.spendly.domain.usecase.auth

import dev.maruffirdaus.spendly.common.model.User
import dev.maruffirdaus.spendly.domain.repository.AuthRepository
import javax.inject.Inject

class GetUserUseCase @Inject constructor(
    private val repository: AuthRepository
) {
    operator fun invoke(): User? = repository.getUser()
}