package dev.maruffirdaus.spendly.data.util

import dev.maruffirdaus.spendly.common.model.Deal
import dev.maruffirdaus.spendly.common.model.Deals
import dev.maruffirdaus.spendly.data.remote.model.ItadResponse

fun ItadResponse.toDeals(): Deals = Deals(
    nextOffset = this.nextOffset ?: 0,
    hasMore = this.hasMore ?: false,
    items = this.list?.map {
        Deal(
            title = it?.title ?: "",
            url = it?.deal?.url ?: "",
            imageUrl = it?.assets?.boxart ?: it?.assets?.banner600 ?: it?.assets?.banner400
            ?: it?.assets?.banner300 ?: it?.assets?.banner145 ?: "",
            cut = it?.deal?.cut ?: 0,
            currency = it?.deal?.regular?.currency ?: "",
            regularPrice = it?.deal?.regular?.amount?.toString()?.toDouble() ?: 0,
            dealPrice = it?.deal?.price?.amount?.toString()?.toDouble() ?: 0,
        )
    } ?: emptyList()
)