package dev.maruffirdaus.spendly.common.constant

import androidx.annotation.StringRes
import dev.maruffirdaus.spendly.R

enum class DealsSources(
    @StringRes val title: Int,
    val id: String? = null,
    @StringRes val providerName: Int? = null,
    val providerUrl: String? = null
) {
    EPIC_GAMES(
        title = R.string.epic_games,
        id = "16",
        providerName = R.string.itad,
        providerUrl = "https://isthereanydeal.com/"
    ),
    GOG(
        title = R.string.gog,
        id = "35",
        providerName = R.string.itad,
        providerUrl = "https://isthereanydeal.com/"
    ),
    STEAM(
        title = R.string.steam,
        id = "61",
        providerName = R.string.itad,
        providerUrl = "https://isthereanydeal.com/"
    )
}