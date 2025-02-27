package dev.maruffirdaus.spendly.ui.deals.component

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
fun DealsTopAppBar(
    onMenuClick: (() -> Unit)?,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(
        title = {
            Text(stringResource(R.string.deals))
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
                onClick = {},
                enabled = false
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_edit),
                    contentDescription = stringResource(R.string.customize)
                )
            }
        }
    )
}

@Preview
@Composable
private fun DealsTopAppBarPreview() {
    SpendlyTheme {
        DealsTopAppBar(
            onMenuClick = {}
        )
    }
}