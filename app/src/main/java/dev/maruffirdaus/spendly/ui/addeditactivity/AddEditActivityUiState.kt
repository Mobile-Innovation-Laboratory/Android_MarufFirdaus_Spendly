package dev.maruffirdaus.spendly.ui.addeditactivity

import dev.maruffirdaus.spendly.common.model.Category
import dev.maruffirdaus.spendly.ui.addeditactivity.constant.ActivityTypes
import java.time.Instant

data class AddEditActivityUiState(
    val title: String = "",
    val date: Long = Instant.now().toEpochMilli(),
    val amount: String = "",
    val amountError: String? = null,
    val activityType: ActivityTypes = ActivityTypes.EXPENSE,
    val categories: List<Category> = emptyList(),
    val selectedCategoryId: String? = null,
    val categoryToEdit: Category? = null,
    val categoryToDelete: Category? = null,
    val isLoading: Boolean = false,
    val isDatePickerDialogVisible: Boolean = false,
    val isAddEditCategoryDialogVisible: Boolean = false,
    val isDeleteCategoryDialogVisible: Boolean = false
)
