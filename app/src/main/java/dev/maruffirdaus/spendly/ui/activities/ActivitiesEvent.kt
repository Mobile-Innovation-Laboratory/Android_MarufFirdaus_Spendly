package dev.maruffirdaus.spendly.ui.activities

import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.common.model.Category

sealed class ActivitiesEvent {
    data class OnSelectedTabIndexChange(val selectedTabIndex: Int) : ActivitiesEvent()

    data class OnCategoryListPositionChange(val index: Int, val offset: Int) : ActivitiesEvent()

    data class OnSelectedCategoryIdChange(val categoryId: String?) : ActivitiesEvent()

    data class OnAddEditCategoryClick(val category: Category? = null) : ActivitiesEvent()
    data class OnSaveCategory(val category: Category, val onSuccess: (() -> Unit)? = null) :
        ActivitiesEvent()

    data object OnCancelAddEditCategory : ActivitiesEvent()

    data class OnDeleteCategoryClick(val category: Category) : ActivitiesEvent()
    data class OnConfirmDeleteCategory(val onSuccess: (() -> Unit)? = null) : ActivitiesEvent()

    data object OnCancelDeleteCategory : ActivitiesEvent()

    data class OnActivityListPositionChange(val index: Int, val offset: Int) : ActivitiesEvent()

    data class OnDeleteActivityClick(val activity: Activity) : ActivitiesEvent()
    data class OnConfirmDeleteActivity(val onSuccess: (() -> Unit)? = null) : ActivitiesEvent()

    data object OnCancelDeleteActivity : ActivitiesEvent()

    data object OnRefreshCategories : ActivitiesEvent()

    data class OnRefreshActivities(val walletId: String, val year: String) : ActivitiesEvent()
}