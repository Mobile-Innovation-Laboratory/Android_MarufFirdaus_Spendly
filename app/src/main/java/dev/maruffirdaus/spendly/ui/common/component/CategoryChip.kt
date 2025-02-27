package dev.maruffirdaus.spendly.ui.common.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CategoryChip(
    title: @Composable () -> Unit,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    onLongClick: (() -> Unit)? = null,
) {
    val hapticFeedback = LocalHapticFeedback.current

    Surface(
        modifier = modifier
            .height(32.dp)
            .clip(CircleShape)
            .combinedClickable(
                onLongClick = if (onLongClick != null) {
                    {
                        hapticFeedback.performHapticFeedback(HapticFeedbackType.LongPress)
                        onLongClick()
                    }
                } else {
                    null
                },
                onClick = onClick
            ),
        shape = CircleShape,
        color = if (selected) {
            MaterialTheme.colorScheme.secondaryContainer
        } else {
            MaterialTheme.colorScheme.surfaceContainerHighest
        },
        contentColor = if (selected) {
            MaterialTheme.colorScheme.onSecondaryContainer
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant
        }
    ) {
        Box(
            modifier = Modifier.padding(horizontal = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            CompositionLocalProvider(
                LocalTextStyle provides MaterialTheme.typography.labelLarge
            ) {
                title()
            }
        }
    }
}

@Preview
@Composable
private fun CategoryChipSelectedPreview() {
    SpendlyTheme {
        CategoryChip(
            title = {
                Text("Food")
            },
            selected = true,
            onClick = {},
            onLongClick = {}
        )
    }
}

@Preview
@Composable
private fun CategoryChipUnselectedPreview() {
    SpendlyTheme {
        CategoryChip(
            title = {
                Text("Food")
            },
            selected = false,
            onClick = {},
            onLongClick = {}
        )
    }
}