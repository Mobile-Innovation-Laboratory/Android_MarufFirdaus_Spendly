package dev.maruffirdaus.spendly.domain.usecase.category

import dev.maruffirdaus.spendly.common.model.Category
import dev.maruffirdaus.spendly.domain.repository.CategoryRepository
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> = repository.getCategories()
}