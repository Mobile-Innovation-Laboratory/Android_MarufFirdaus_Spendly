package dev.maruffirdaus.spendly.ui.addeditactivity

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.AddEditActivity
import dev.maruffirdaus.spendly.ui.addeditactivity.component.AddEditActivityTopAppBar
import dev.maruffirdaus.spendly.ui.addeditactivity.constant.ActivityTypes
import dev.maruffirdaus.spendly.ui.common.component.AddEditCategoryDialog
import dev.maruffirdaus.spendly.ui.common.component.CategoryList
import dev.maruffirdaus.spendly.ui.common.component.LeftGradient
import dev.maruffirdaus.spendly.ui.common.component.RightGradient
import dev.maruffirdaus.spendly.ui.common.component.TopGradient
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import dev.maruffirdaus.spendly.ui.util.formatDate
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddEditActivityScreen(
    walletId: String,
    currency: String,
    uiState: AddEditActivityUiState,
    onEvent: (AddEditActivityEvent) -> Unit,
    activityId: String? = null,
    navController: NavController = rememberNavController()
) {
    val context = LocalContext.current
    var isSaveButtonEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.title, uiState.amount) {
        isSaveButtonEnabled = uiState.title.isNotBlank() && uiState.amount.isNotBlank()
    }

    LaunchedEffect(Unit) {
        if (activityId != null) {
            onEvent(AddEditActivityEvent.OnRefreshActivity(activityId))
        }

        onEvent(AddEditActivityEvent.OnRefreshCategories)
    }

    Scaffold(
        topBar = {
            AddEditActivityTopAppBar(
                isEdit = activityId != null,
                isSaveButtonEnabled = isSaveButtonEnabled,
                onCloseClick = {
                    navController.popBackStack(
                        route = AddEditActivity(
                            walletId = walletId,
                            currency = currency,
                            activityId = activityId
                        ),
                        inclusive = true
                    )
                },
                onSaveClick = {
                    if (uiState.amount.toLongOrNull() != null) {
                        onEvent(
                            AddEditActivityEvent.OnSaveActivity(
                                walletId = walletId,
                                activityId = activityId,
                                onSuccess = {
                                    navController.popBackStack(
                                        route = AddEditActivity(
                                            walletId = walletId,
                                            currency = currency,
                                            activityId = activityId
                                        ),
                                        inclusive = true
                                    )
                                }
                            )
                        )
                    } else {
                        onEvent(
                            AddEditActivityEvent.OnAmountError(
                                context.getString(R.string.amount_input_error_message)
                            )
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        val datePickerState = rememberDatePickerState()

        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            TopGradient(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
                    .height(16.dp)
                    .zIndex(1f)
            )
            AnimatedVisibility(
                visible = uiState.isLoading,
                modifier = Modifier
                    .padding(innerPadding)
                    .align(Alignment.Center),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CircularProgressIndicator()
            }
            AnimatedVisibility(
                visible = !uiState.isLoading,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                Column(
                    modifier = Modifier
                        .imePadding()
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(innerPadding)
                        .padding(vertical = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var isAmountError by remember { mutableStateOf(false) }

                    LaunchedEffect(uiState.amountError) {
                        isAmountError = uiState.amountError != null
                    }

                    OutlinedTextField(
                        value = uiState.title,
                        onValueChange = {
                            onEvent(AddEditActivityEvent.OnTitleChange(it))
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .widthIn(max = 600.dp)
                            .fillMaxWidth(),
                        label = {
                            Text(stringResource(R.string.title))
                        },
                        keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Sentences),
                        singleLine = true
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = formatDate(uiState.date),
                        onValueChange = {},
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .widthIn(max = 600.dp)
                            .fillMaxWidth(),
                        readOnly = true,
                        label = {
                            Text(stringResource(R.string.date))
                        },
                        trailingIcon = {
                            IconButton(
                                onClick = {
                                    onEvent(AddEditActivityEvent.OnSelectDateClick)
                                }
                            ) {
                                Icon(
                                    painter = painterResource(R.drawable.ic_edit_calendar),
                                    contentDescription = null
                                )
                            }
                        },
                        singleLine = true
                    )
                    Spacer(Modifier.height(16.dp))
                    OutlinedTextField(
                        value = uiState.amount,
                        onValueChange = {
                            onEvent(AddEditActivityEvent.OnAmountChange(it))
                        },
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .widthIn(max = 600.dp)
                            .fillMaxWidth(),
                        label = {
                            Text(stringResource(R.string.amount))
                        },
                        prefix = {
                            Text(currency)
                        },
                        supportingText = if (uiState.amountError != null) {
                            {
                                Text(uiState.amountError)
                            }
                        } else {
                            null
                        },
                        isError = isAmountError,
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Number
                        ),
                        singleLine = true
                    )
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.type),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .widthIn(max = 600.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(8.dp))
                    Column(
                        modifier = Modifier.selectableGroup()
                    ) {
                        ActivityTypes.entries.forEach { type ->
                            Row(
                                modifier = Modifier
                                    .selectable(
                                        selected = type == uiState.activityType,
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        role = Role.RadioButton
                                    ) {
                                        onEvent(AddEditActivityEvent.OnActivityTypeChange(type))
                                    }
                                    .padding(horizontal = 16.dp)
                                    .widthIn(max = 600.dp)
                                    .fillMaxWidth()
                                    .height(56.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = type == uiState.activityType,
                                    onClick = null
                                )
                                Spacer(Modifier.width(16.dp))
                                Text(
                                    text = stringResource(type.text),
                                    color = MaterialTheme.colorScheme.onSurface,
                                    overflow = TextOverflow.Ellipsis,
                                    maxLines = 1,
                                    style = MaterialTheme.typography.bodyLarge
                                )
                            }
                        }
                    }
                    Spacer(Modifier.height(16.dp))
                    Text(
                        text = stringResource(R.string.category),
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .widthIn(max = 600.dp)
                            .fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodySmall
                    )
                    Spacer(Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .widthIn(max = 632.dp)
                            .fillMaxWidth()
                            .height(56.dp)
                    ) {
                        LeftGradient(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(16.dp)
                                .align(Alignment.CenterStart)
                                .zIndex(1f)
                        )
                        CategoryList(
                            categories = uiState.categories,
                            selectedCategoryId = uiState.selectedCategoryId,
                            onCategoryClick = { category ->
                                onEvent(AddEditActivityEvent.OnSelectedCategoryIdChange(category?.categoryId))
                            },
                            onAddEditCategoryClick = { category ->
                                onEvent(AddEditActivityEvent.OnAddEditCategoryClick(category))
                            },
                            onDeleteCategoryClick = { category ->
                                onEvent(AddEditActivityEvent.OnDeleteCategoryClick(category))
                            },
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(horizontal = 16.dp)
                        )
                        RightGradient(
                            modifier = Modifier
                                .fillMaxHeight()
                                .width(16.dp)
                                .align(Alignment.CenterEnd)
                                .zIndex(1f)
                        )
                    }
                }
            }
        }

        if (uiState.isDatePickerDialogVisible) {
            DatePickerDialog(
                onDismissRequest = {
                    onEvent(AddEditActivityEvent.OnDismissSelectDate)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(
                                AddEditActivityEvent.OnDateChange(
                                    datePickerState.selectedDateMillis ?: Instant.now()
                                        .toEpochMilli()
                                )
                            )
                            onEvent(AddEditActivityEvent.OnDismissSelectDate)
                        }
                    ) {
                        Text(stringResource(R.string.select))
                    }
                },
                dismissButton = {
                    TextButton(
                        onClick = {
                            onEvent(AddEditActivityEvent.OnDismissSelectDate)
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            ) {
                DatePicker(
                    state = datePickerState
                )
            }
        }

        if (uiState.isAddEditCategoryDialogVisible) {
            AddEditCategoryDialog(
                onDismissRequest = {
                    onEvent(AddEditActivityEvent.OnCancelAddEditCategory)
                },
                onSave = { category ->
                    onEvent(
                        AddEditActivityEvent.OnSaveCategory(
                            category = category,
                            onSuccess = {
                                onEvent(AddEditActivityEvent.OnRefreshCategories)
                            }
                        )
                    )
                },
                category = uiState.categoryToEdit
            )
        }

        if (uiState.isDeleteCategoryDialogVisible) {
            AlertDialog(
                onDismissRequest = {
                    onEvent(AddEditActivityEvent.OnCancelDeleteCategory)
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onEvent(
                                AddEditActivityEvent.OnConfirmDeleteCategory(
                                    onSuccess = {
                                        onEvent(AddEditActivityEvent.OnRefreshCategories)
                                        onEvent(
                                            AddEditActivityEvent.OnSelectedCategoryIdChange(
                                                null
                                            )
                                        )
                                    }
                                )
                            )
                        }
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                },
                modifier = Modifier
                    .widthIn(max = 320.dp)
                    .fillMaxWidth(),
                dismissButton = {
                    TextButton(
                        onClick = {
                            onEvent(AddEditActivityEvent.OnCancelDeleteCategory)
                        }
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                },
                title = {
                    Text(stringResource(R.string.delete_category_title))
                },
                text = {
                    Text(stringResource(R.string.delete_category_message))
                }
            )
        }
    }
}

@Preview
@Composable
private fun AddEditActivityScreenPreview() {
    SpendlyTheme {
        AddEditActivityScreen(
            walletId = "",
            currency = "Rp",
            uiState = AddEditActivityUiState(),
            onEvent = {}
        )
    }
}