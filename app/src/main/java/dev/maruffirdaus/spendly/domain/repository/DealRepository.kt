package dev.maruffirdaus.spendly.domain.repository

import dev.maruffirdaus.spendly.common.model.Deals

interface DealRepository {
    suspend fun getDealsFromItad(country: String, offset: Int, shops: IntArray): Result<Deals>
}