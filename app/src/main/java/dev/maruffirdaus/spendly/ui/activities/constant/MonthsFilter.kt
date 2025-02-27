package dev.maruffirdaus.spendly.ui.activities.constant

import androidx.annotation.StringRes
import dev.maruffirdaus.spendly.R

enum class MonthsFilter(
    val id: String?,
    @StringRes val title: Int
) {
    ALL(null, R.string.all),
    JAN("01", R.string.jan),
    FEB("02", R.string.feb),
    MAR("03", R.string.mar),
    APR("04", R.string.apr),
    MAY("05", R.string.may),
    JUN("06", R.string.jun),
    JUL("07", R.string.jul),
    AUG("08", R.string.aug),
    SEP("09", R.string.sep),
    OCT("10", R.string.oct),
    NOV("11", R.string.nov),
    DEC("12", R.string.dec)
}