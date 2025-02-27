package dev.maruffirdaus.spendly.ui.activities

import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.common.model.Category
import dev.maruffirdaus.spendly.ui.activities.constant.MonthsFilter
import java.time.LocalDateTime

data class ActivitiesUiState(
    val selectedTabIndex: Int = LocalDateTime.now().monthValue,
    val isCategoryListVisible: Boolean = true,
    val categories: List<Category> = emptyList(),
    val categoryListPosition: Pair<Int, Int> = Pair(0, 0),
    val selectedCategoryId: String? = null,
    val categoryToEdit: Category? = null,
    val categoryToDelete: Category? = null,
    val selectedMonthFilter: MonthsFilter = MonthsFilter.entries[selectedTabIndex],
    val activities: List<Activity> = emptyList(),
    val activityListPosition: Pair<Int, Int> = Pair(0, 0),
    val activityToDelete: Activity? = null,
    val isActivitiesLoading: Boolean = false,
    val isAddEditCategoryDialogVisible: Boolean = false,
    val isDeleteCategoryDialogVisible: Boolean = false,
    val isDeleteActivityDialogVisible: Boolean = false
)