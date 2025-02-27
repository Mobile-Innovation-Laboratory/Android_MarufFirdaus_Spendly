package dev.maruffirdaus.spendly.ui.addeditactivity

import dev.maruffirdaus.spendly.common.model.Category
import dev.maruffirdaus.spendly.ui.addeditactivity.constant.ActivityTypes

sealed class AddEditActivityEvent {
    data class OnSaveActivity(
        val walletId: String,
        val activityId: String? = null,
        val onSuccess: (() -> Unit)? = null
    ) : AddEditActivityEvent()

    data class OnTitleChange(val title: String) : AddEditActivityEvent()

    data class OnDateChange(val date: Long) : AddEditActivityEvent()

    data object OnSelectDateClick : AddEditActivityEvent()
    data object OnDismissSelectDate : AddEditActivityEvent()

    data class OnAmountChange(val amount: String) : AddEditActivityEvent()
    data class OnAmountError(val error: String) : AddEditActivityEvent()

    data class OnActivityTypeChange(val type: ActivityTypes) : AddEditActivityEvent()

    data class OnSelectedCategoryIdChange(val categoryId: String?) : AddEditActivityEvent()

    data class OnAddEditCategoryClick(val category: Category? = null) : AddEditActivityEvent()
    data class OnSaveCategory(val category: Category, val onSuccess: (() -> Unit)? = null) :
        AddEditActivityEvent()

    data object OnCancelAddEditCategory : AddEditActivityEvent()

    data class OnDeleteCategoryClick(val category: Category) : AddEditActivityEvent()
    data class OnConfirmDeleteCategory(val onSuccess: (() -> Unit)? = null) : AddEditActivityEvent()
    data object OnCancelDeleteCategory : AddEditActivityEvent()

    data class OnRefreshActivity(val activityId: String) : AddEditActivityEvent()

    data object OnRefreshCategories : AddEditActivityEvent()
}