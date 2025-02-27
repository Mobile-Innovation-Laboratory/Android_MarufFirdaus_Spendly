package dev.maruffirdaus.spendly.ui.activities

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maruffirdaus.spendly.domain.usecase.activity.DeleteActivityUseCase
import dev.maruffirdaus.spendly.domain.usecase.activity.GetActivitiesUseCase
import dev.maruffirdaus.spendly.domain.usecase.activity.SyncActivitiesUseCase
import dev.maruffirdaus.spendly.domain.usecase.auth.GetUserUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.AddEditCategoryUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.DeleteCategoryUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.GetCategoriesUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.SyncCategoriesUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.GetDataSyncEnabledUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.SyncWalletsUseCase
import dev.maruffirdaus.spendly.ui.activities.constant.MonthsFilter
import dev.maruffirdaus.spendly.ui.util.ConnectivityObserver
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ActivitiesViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getDataSyncEnabledUseCase: GetDataSyncEnabledUseCase,
    private val syncWalletsUseCase: SyncWalletsUseCase,
    private val addEditCategoryUseCase: AddEditCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val syncCategoriesUseCase: SyncCategoriesUseCase,
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val deleteActivityUseCase: DeleteActivityUseCase,
    private val syncActivitiesUseCase: SyncActivitiesUseCase,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private val _uiState = MutableStateFlow(ActivitiesUiState())
    val uiState = _uiState.asStateFlow()

    private val isConnected = connectivityObserver.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    fun onEvent(event: ActivitiesEvent) {
        when (event) {
            is ActivitiesEvent.OnSelectedTabIndexChange -> {
                _uiState.update {
                    it.copy(
                        selectedTabIndex = event.selectedTabIndex,
                        selectedMonthFilter = MonthsFilter.entries[event.selectedTabIndex]
                    )
                }
            }

            is ActivitiesEvent.OnCategoryListPositionChange -> {
                _uiState.update {
                    it.copy(
                        categoryListPosition = Pair(event.index, event.offset)
                    )
                }
            }

            is ActivitiesEvent.OnSelectedCategoryIdChange -> {
                _uiState.update {
                    it.copy(
                        selectedCategoryId = event.categoryId
                    )
                }
            }

            is ActivitiesEvent.OnAddEditCategoryClick -> {
                _uiState.update {
                    it.copy(
                        categoryToEdit = event.category,
                        isAddEditCategoryDialogVisible = true
                    )
                }
            }

            is ActivitiesEvent.OnSaveCategory -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isAddEditCategoryDialogVisible = false
                        )
                    }
                    addEditCategoryUseCase(event.category)
                    event.onSuccess?.invoke()
                    _uiState.update {
                        it.copy(
                            categoryToEdit = null
                        )
                    }
                }
            }

            is ActivitiesEvent.OnCancelAddEditCategory -> {
                _uiState.update {
                    it.copy(
                        categoryToEdit = null,
                        isAddEditCategoryDialogVisible = false
                    )
                }
            }

            is ActivitiesEvent.OnDeleteCategoryClick -> {
                _uiState.update {
                    it.copy(
                        categoryToDelete = event.category,
                        isDeleteCategoryDialogVisible = true
                    )
                }
            }

            is ActivitiesEvent.OnConfirmDeleteCategory -> {
                uiState.value.categoryToDelete?.let { category ->
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(
                                isDeleteCategoryDialogVisible = false
                            )
                        }
                        deleteCategoryUseCase(category, getDataSyncEnabledUseCase())
                        event.onSuccess?.invoke()
                        _uiState.update {
                            it.copy(
                                categoryToDelete = null
                            )
                        }
                    }
                }
            }

            is ActivitiesEvent.OnCancelDeleteCategory -> {
                _uiState.update {
                    it.copy(
                        categoryToDelete = null,
                        isDeleteCategoryDialogVisible = false
                    )
                }
            }

            is ActivitiesEvent.OnActivityListPositionChange -> {
                _uiState.update {
                    it.copy(
                        isCategoryListVisible = event.index == 0 && event.offset == 0,
                        activityListPosition = Pair(event.index, event.offset)
                    )
                }
            }

            is ActivitiesEvent.OnDeleteActivityClick -> {
                _uiState.update {
                    it.copy(
                        activityToDelete = event.activity,
                        isDeleteActivityDialogVisible = true
                    )
                }
            }

            is ActivitiesEvent.OnConfirmDeleteActivity -> {
                uiState.value.activityToDelete?.let { activity ->
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(
                                isDeleteActivityDialogVisible = false
                            )
                        }
                        deleteActivityUseCase(activity, getDataSyncEnabledUseCase())
                        event.onSuccess?.invoke()
                        _uiState.update {
                            it.copy(
                                activityToDelete = null
                            )
                        }
                    }
                }
            }

            is ActivitiesEvent.OnCancelDeleteActivity -> {
                _uiState.update {
                    it.copy(
                        activityToDelete = null,
                        isDeleteActivityDialogVisible = false
                    )
                }
            }

            is ActivitiesEvent.OnRefreshCategories -> {
                viewModelScope.launch {
                    if (isConnected.first()) {
                        val user = getUserUseCase()

                        if (user != null && getDataSyncEnabledUseCase()) {
                            syncCategoriesUseCase(user.userId)
                        }
                    }

                    _uiState.update {
                        it.copy(
                            categories = getCategoriesUseCase()
                        )
                    }
                }
            }

            is ActivitiesEvent.OnRefreshActivities -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isActivitiesLoading = true
                        )
                    }

                    if (isConnected.first()) {
                        val user = getUserUseCase()

                        if (user != null && getDataSyncEnabledUseCase()) {
                            syncWalletsUseCase(user.userId)
                            syncCategoriesUseCase(user.userId)
                            syncActivitiesUseCase(user.userId)
                        }
                    }

                    val activities = getActivitiesUseCase(
                        walletId = event.walletId,
                        categoryId = uiState.value.selectedCategoryId,
                        year = event.year,
                        month = uiState.value.selectedMonthFilter.id
                    )

                    _uiState.update {
                        it.copy(
                            activities = activities,
                            isActivitiesLoading = false
                        )
                    }
                }
            }
        }
    }
}