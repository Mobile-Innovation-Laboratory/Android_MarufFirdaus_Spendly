package dev.maruffirdaus.spendly.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import dev.maruffirdaus.spendly.ui.theme.fadeColors

@Composable
fun TopGradient(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Brush.verticalGradient(fadeColors()))
    )
}

@Preview
@Composable
private fun TopGradientPreview() {
    SpendlyTheme {
        TopGradient(
            modifier = Modifier
                .fillMaxWidth()
                .height(16.dp)
        )
    }
}