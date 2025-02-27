package dev.maruffirdaus.spendly.data.util

import dev.maruffirdaus.spendly.common.model.Category
import dev.maruffirdaus.spendly.data.local.model.CategoryEntity

fun CategoryEntity.toCategory(): Category = Category(
    title = this.title,
    categoryId = this.categoryId
)