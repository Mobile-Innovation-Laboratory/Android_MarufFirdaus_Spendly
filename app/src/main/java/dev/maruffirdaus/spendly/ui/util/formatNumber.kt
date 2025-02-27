package dev.maruffirdaus.spendly.ui.util

import java.text.NumberFormat

fun formatNumber(number: Number): String {
    val formatter = NumberFormat.getInstance()

    return formatter.format(number)
}