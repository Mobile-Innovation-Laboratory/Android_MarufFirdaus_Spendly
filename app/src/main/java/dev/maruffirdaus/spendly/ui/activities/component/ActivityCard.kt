package dev.maruffirdaus.spendly.ui.activities.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.ui.theme.LocalExtendedColorScheme
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import dev.maruffirdaus.spendly.ui.util.formatDate
import dev.maruffirdaus.spendly.ui.util.formatNumber

@Composable
fun ActivityCard(
    currency: String,
    activity: Activity,
    onClick: (Activity) -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = {
            onClick(activity)
        },
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Spacer(Modifier.height(24.dp))
        Row(
            modifier = Modifier.padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(24.dp))
                    .background(
                        if (activity.amount < 0) {
                            LocalExtendedColorScheme.current.expenseContainer
                        } else {
                            LocalExtendedColorScheme.current.incomeContainer
                        }
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_payments),
                    contentDescription = null,
                    tint = if (activity.amount < 0) {
                        LocalExtendedColorScheme.current.onExpenseContainer
                    } else {
                        LocalExtendedColorScheme.current.onIncomeContainer
                    }
                )
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    text = activity.title,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = formatDate(activity.date),
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                    style = MaterialTheme.typography.labelLarge
                )
            }
        }
        Spacer(Modifier.height(24.dp))
        Text(
            text = if (activity.amount < 0) {
                buildString {
                    append(currency)
                    append(" -")
                    append(formatNumber(activity.amount * -1))
                }
            } else {
                buildString {
                    append(currency)
                    append(" ")
                    append(formatNumber(activity.amount))
                }
            },
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .align(Alignment.End),
            color = if (activity.amount < 0) {
                LocalExtendedColorScheme.current.expense
            } else {
                LocalExtendedColorScheme.current.income
            },
            overflow = TextOverflow.Ellipsis,
            maxLines = 1,
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun ActivityCardPreview() {
    SpendlyTheme {
        ActivityCard(
            currency = "Rp",
            activity = Activity(
                walletId = "",
                title = "Pay YouTube Premium",
                amount = -59000,
                date = "2024-04-04"
            ),
            onClick = {}
        )
    }
}