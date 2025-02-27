package dev.maruffirdaus.spendly.ui.util

import java.time.LocalDate
import java.time.ZoneId

fun getMillisFromDate(date: String): Long {
    val localDate = LocalDate.parse(date)

    return localDate.atStartOfDay(ZoneId.of("UTC")).toInstant().toEpochMilli()
}