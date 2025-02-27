package dev.maruffirdaus.spendly.ui.activities.component

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesTopAppBar(
    selectedYear: String,
    onMenuClick: (() -> Unit)?,
    onYearPickerClick: (() -> Unit),
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(selectedYear)
        },
        modifier = modifier,
        navigationIcon = {
            onMenuClick?.let {
                IconButton(
                    onClick = it
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_menu),
                        contentDescription = stringResource(R.string.menu)
                    )
                }
            }
        },
        actions = {
            IconButton(
                onClick = onYearPickerClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit_calendar),
                    contentDescription = stringResource(R.string.year_picker)
                )
            }
        }
    )
}

@Preview
@Composable
private fun ActivitiesTopAppBarPreview() {
    SpendlyTheme {
        ActivitiesTopAppBar(
            selectedYear = "2025",
            onMenuClick = {},
            onYearPickerClick = {}
        )
    }
}