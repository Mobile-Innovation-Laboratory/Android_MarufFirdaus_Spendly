package dev.maruffirdaus.spendly.ui.addeditactivity.component

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditActivityTopAppBar(
    isEdit: Boolean,
    isSaveButtonEnabled: Boolean,
    onCloseClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                if (isEdit) {
                    stringResource(R.string.edit_activity)
                } else {
                    stringResource(R.string.add_activity)
                }
            )
        },
        navigationIcon = {
            IconButton(
                onClick = onCloseClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_close),
                    contentDescription = stringResource(R.string.close)
                )
            }
        },
        actions = {
            TextButton(
                onClick = onSaveClick,
                enabled = isSaveButtonEnabled
            ) {
                Text(stringResource(R.string.save))
            }
        }
    )
}

@Preview
@Composable
private fun AddEditActivityTopAppBarPreview() {
    SpendlyTheme {
        AddEditActivityTopAppBar(
            isEdit = false,
            isSaveButtonEnabled = true,
            onCloseClick = {},
            onSaveClick = {},
        )
    }
}