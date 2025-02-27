package dev.maruffirdaus.spendly.ui.common.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun MessageCard(
    text: @Composable () -> Unit,
    action: @Composable () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            ) {
                text()
            }
        }
        Spacer(Modifier.height(24.dp))
        Box(
            modifier = Modifier
                .padding(horizontal = 24.dp)
                .align(Alignment.CenterHorizontally)
        ) {
            action()
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Preview
@Composable
private fun MessageCardPreview() {
    SpendlyTheme {
        MessageCard(
            text = {
                Text(stringResource(R.string.empty_activity_list_message))
            },
            action = {
                OutlinedButton(
                    onClick = {}
                ) {
                    Text(stringResource(R.string.add_activity))
                }
            }
        )
    }
}