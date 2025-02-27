package dev.maruffirdaus.spendly.ui.deals.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import dev.maruffirdaus.spendly.common.model.Deal
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import dev.maruffirdaus.spendly.ui.util.formatNumber

@Composable
fun DealCard(
    deal: Deal,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        val density = LocalDensity.current

        Row {
            var imageHeight by remember { mutableStateOf(0.dp) }

            AsyncImage(
                model = deal.imageUrl,
                contentDescription = null,
                modifier = Modifier
                    .width(150.dp)
                    .heightIn(min = 200.dp)
                    .onSizeChanged { size ->
                        with(density) {
                            imageHeight = size.height.toDp()
                        }
                    }
                    .clip(RoundedCornerShape(28.dp)),
                contentScale = ContentScale.FillWidth
            )
            Column(
                modifier = Modifier
                    .height(imageHeight)
                    .padding(24.dp)
            ) {
                Box(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = deal.title,
                        overflow = TextOverflow.Ellipsis,
                        style = MaterialTheme.typography.titleMedium
                    )
                }
                Spacer(Modifier.height(24.dp))
                Text(
                    text = buildString {
                        append(deal.currency)
                        append(" ")
                        append(formatNumber(deal.regularPrice))
                    },
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textDecoration = TextDecoration.LineThrough,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = buildString {
                        append(deal.currency)
                        append(" ")
                        append(formatNumber(deal.dealPrice))
                    },
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }
    }
}

@Preview
@Composable
private fun DealCardPreview() {
    SpendlyTheme {
        DealCard(
            deal = Deal(
                title = "NBA 2K25",
                url = "",
                imageUrl = "",
                cut = 67,
                currency = "IDR",
                regularPrice = 799000.00,
                dealPrice = 263670.00,
            ),
            onClick = {}
        )
    }
}