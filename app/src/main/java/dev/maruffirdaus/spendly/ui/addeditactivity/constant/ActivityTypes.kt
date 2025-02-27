package dev.maruffirdaus.spendly.ui.addeditactivity.constant

import androidx.annotation.StringRes
import dev.maruffirdaus.spendly.R

enum class ActivityTypes(
    @StringRes val text: Int
) {
    INCOME(R.string.income),
    EXPENSE(R.string.expense)
}