package dev.maruffirdaus.spendly.ui.common.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import dev.maruffirdaus.spendly.ui.theme.fadeColors

@Composable
fun LeftGradient(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.background(Brush.horizontalGradient(fadeColors()))
    )
}

@Preview
@Composable
private fun LeftGradientPreview() {
    SpendlyTheme {
        LeftGradient(
            modifier = Modifier
                .width(16.dp)
                .fillMaxHeight()
        )
    }
}