package dev.maruffirdaus.spendly.domain.usecase.deal

import dev.maruffirdaus.spendly.common.model.Deals
import dev.maruffirdaus.spendly.common.constant.DealsSources
import dev.maruffirdaus.spendly.domain.repository.DealRepository
import javax.inject.Inject

class GetDealsUseCase @Inject constructor(
    private val repository: DealRepository
) {
    suspend operator fun invoke(source: DealsSources, country: String, offset: Int): Result<Deals> {
        return when (source) {
            DealsSources.EPIC_GAMES -> {
                repository.getDealsFromItad(
                    country = country,
                    offset = offset,
                    shops = intArrayOf(source.id!!.toInt())
                )
            }

            DealsSources.GOG -> {
                repository.getDealsFromItad(
                    country = country,
                    offset = offset,
                    shops = intArrayOf(source.id!!.toInt())
                )
            }

            DealsSources.STEAM -> {
                repository.getDealsFromItad(
                    country = country,
                    offset = offset,
                    shops = intArrayOf(source.id!!.toInt())
                )
            }
        }
    }
}