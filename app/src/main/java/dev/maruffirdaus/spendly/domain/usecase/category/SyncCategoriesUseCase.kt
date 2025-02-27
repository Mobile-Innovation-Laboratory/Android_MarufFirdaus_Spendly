package dev.maruffirdaus.spendly.domain.usecase.category

import dev.maruffirdaus.spendly.domain.repository.FirebaseCategoryRepository
import javax.inject.Inject

class SyncCategoriesUseCase @Inject constructor(
    private val repository: FirebaseCategoryRepository
) {
    suspend operator fun invoke(userId: String) = repository.syncCategories(userId)
}