package dev.maruffirdaus.spendly.ui.main.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import java.time.LocalDateTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun YearPickerDialog(
    onDismissRequest: () -> Unit,
    onYearSelected: (Int) -> Unit,
    currentYear: Int = LocalDateTime.now().year
) {
    Dialog(
        onDismissRequest = onDismissRequest
    ) {
        Surface(
            modifier = Modifier.widthIn(max = 420.dp),
            shape = RoundedCornerShape(28.dp),
            color = MaterialTheme.colorScheme.surfaceContainerHigh
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                var selectedYear by remember { mutableStateOf(currentYear.toString()) }
                var isDropdownMenuExpanded by remember { mutableStateOf(false) }

                Text(
                    text = stringResource(R.string.select_year),
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.height(16.dp))
                ExposedDropdownMenuBox(
                    expanded = isDropdownMenuExpanded,
                    onExpandedChange = {
                        isDropdownMenuExpanded = !isDropdownMenuExpanded
                    }
                ) {
                    val density = LocalDensity.current
                    val years = (1900..2100).toList()
                    var textFieldWidth by remember { mutableStateOf(0.dp) }
                    val yearListState = rememberLazyListState()

                    LaunchedEffect(isDropdownMenuExpanded) {
                        if (isDropdownMenuExpanded) {
                            yearListState.scrollToItem(
                                years.indexOf(
                                    selectedYear.toIntOrNull() ?: currentYear
                                )
                            )
                        }
                    }

                    OutlinedTextField(
                        value = selectedYear,
                        onValueChange = {
                            selectedYear = it
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .onGloballyPositioned { layoutCoordinates ->
                                with(density) {
                                    textFieldWidth = layoutCoordinates.size.width.toDp()
                                }
                            }
                            .menuAnchor(
                                type = MenuAnchorType.PrimaryEditable
                            ),
                        label = {
                            Text(stringResource(R.string.year))
                        },
                        trailingIcon = {
                            ExposedDropdownMenuDefaults.TrailingIcon(isDropdownMenuExpanded)
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )
                    ExposedDropdownMenu(
                        expanded = isDropdownMenuExpanded,
                        onDismissRequest = {
                            isDropdownMenuExpanded = false
                        }
                    ) {
                        LazyColumn(
                            modifier = Modifier
                                .width(textFieldWidth)
                                .height(240.dp),
                            state = yearListState
                        ) {
                            items(years) { year ->
                                DropdownMenuItem(
                                    text = {
                                        Text(year.toString())
                                    },
                                    onClick = {
                                        selectedYear = year.toString()
                                        isDropdownMenuExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
                Spacer(Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = {
                            onYearSelected(LocalDateTime.now().year)
                            onDismissRequest()
                        }
                    ) {
                        Text(stringResource(R.string.reset))
                    }
                    Row {
                        TextButton(
                            onClick = onDismissRequest
                        ) {
                            Text(stringResource(R.string.cancel))
                        }
                        Spacer(Modifier.width(8.dp))
                        TextButton(
                            onClick = {
                                val year = selectedYear.toIntOrNull() ?: currentYear
                                onYearSelected(
                                    if (year < 1900) 1900 else if (2100 < year) 2100 else year
                                )
                                onDismissRequest()
                            }
                        ) {
                            Text(stringResource(R.string.select))
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun YearPickerDialogPreview() {
    SpendlyTheme {
        YearPickerDialog(
            onDismissRequest = {},
            onYearSelected = {}
        )
    }
}