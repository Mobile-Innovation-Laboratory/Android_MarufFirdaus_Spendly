package dev.maruffirdaus.spendly.ui.util

import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun getDateFromMillis(millis: Long): String {
    val localDate = Instant.ofEpochMilli(millis)
        .atZone(ZoneId.systemDefault())
        .toLocalDate()
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    return formatter.format(localDate)
}