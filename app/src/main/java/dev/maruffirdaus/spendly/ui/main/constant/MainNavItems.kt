package dev.maruffirdaus.spendly.ui.main.constant

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import dev.maruffirdaus.spendly.R

enum class MainNavItems(
    @DrawableRes val selectedIcon: Int,
    @DrawableRes val unselectedIcon: Int,
    @StringRes val label: Int
) {
    HOME(R.drawable.ic_home_filled, R.drawable.ic_home, R.string.home),
    ACTIVITIES(R.drawable.ic_receipt_long_filled, R.drawable.ic_receipt_long, R.string.activities),
    DEALS(R.drawable.ic_sell_filled, R.drawable.ic_sell, R.string.deals)
}