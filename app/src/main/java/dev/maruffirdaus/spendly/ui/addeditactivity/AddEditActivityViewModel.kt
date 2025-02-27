package dev.maruffirdaus.spendly.ui.addeditactivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.domain.usecase.activity.AddEditActivityUseCase
import dev.maruffirdaus.spendly.domain.usecase.activity.GetActivityUseCase
import dev.maruffirdaus.spendly.domain.usecase.activity.SyncActivitiesUseCase
import dev.maruffirdaus.spendly.domain.usecase.auth.GetUserUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.AddEditCategoryUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.DeleteCategoryUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.GetCategoriesUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.SyncCategoriesUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.GetDataSyncEnabledUseCase
import dev.maruffirdaus.spendly.ui.addeditactivity.constant.ActivityTypes
import dev.maruffirdaus.spendly.ui.util.ConnectivityObserver
import dev.maruffirdaus.spendly.ui.util.getDateFromMillis
import dev.maruffirdaus.spendly.ui.util.getMillisFromDate
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.uuid.ExperimentalUuidApi
import kotlin.uuid.Uuid

@HiltViewModel
class AddEditActivityViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getDataSyncEnabledUseCase: GetDataSyncEnabledUseCase,
    private val addEditCategoryUseCase: AddEditCategoryUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val deleteCategoryUseCase: DeleteCategoryUseCase,
    private val syncCategoriesUseCase: SyncCategoriesUseCase,
    private val addEditActivityUseCase: AddEditActivityUseCase,
    private val getActivityUseCase: GetActivityUseCase,
    private val syncActivitiesUseCase: SyncActivitiesUseCase,
    connectivityObserver: ConnectivityObserver
) : ViewModel() {
    private val _uiState = MutableStateFlow(AddEditActivityUiState())
    val uiState = _uiState.asStateFlow()

    private val isConnected = connectivityObserver.isConnected
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)

    @OptIn(ExperimentalUuidApi::class)
    fun onEvent(event: AddEditActivityEvent) {
        when (event) {
            is AddEditActivityEvent.OnSaveActivity -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }
                    addEditActivityUseCase(
                        Activity(
                            walletId = event.walletId,
                            title = uiState.value.title,
                            date = getDateFromMillis(uiState.value.date),
                            amount = uiState.value.amount.toLong().let {
                                if (uiState.value.activityType == ActivityTypes.EXPENSE) {
                                    it * -1
                                } else {
                                    it
                                }
                            },
                            activityId = event.activityId ?: Uuid.random().toString(),
                            categoryId = uiState.value.selectedCategoryId
                        )
                    )

                    if (isConnected.first()) {
                        val user = getUserUseCase()

                        if (user != null && getDataSyncEnabledUseCase()) {
                            syncActivitiesUseCase(user.userId)
                        }
                    }

                    event.onSuccess?.invoke()
                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }

            is AddEditActivityEvent.OnTitleChange -> {
                _uiState.update {
                    it.copy(
                        title = event.title
                    )
                }
            }

            is AddEditActivityEvent.OnDateChange -> {
                _uiState.update {
                    it.copy(
                        date = event.date
                    )
                }
            }

            is AddEditActivityEvent.OnSelectDateClick -> {
                _uiState.update {
                    it.copy(
                        isDatePickerDialogVisible = true
                    )
                }
            }

            is AddEditActivityEvent.OnDismissSelectDate -> {
                _uiState.update {
                    it.copy(
                        isDatePickerDialogVisible = false
                    )
                }
            }

            is AddEditActivityEvent.OnAmountChange -> {
                _uiState.update {
                    it.copy(
                        amount = event.amount
                    )
                }
            }

            is AddEditActivityEvent.OnAmountError -> {
                _uiState.update {
                    it.copy(
                        amountError = event.error
                    )
                }
            }

            is AddEditActivityEvent.OnActivityTypeChange -> {
                _uiState.update {
                    it.copy(
                        activityType = event.type
                    )
                }
            }

            is AddEditActivityEvent.OnSelectedCategoryIdChange -> {
                _uiState.update {
                    it.copy(
                        selectedCategoryId = event.categoryId
                    )
                }
            }

            is AddEditActivityEvent.OnAddEditCategoryClick -> {
                _uiState.update {
                    it.copy(
                        categoryToEdit = event.category,
                        isAddEditCategoryDialogVisible = true
                    )
                }
            }

            is AddEditActivityEvent.OnSaveCategory -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isAddEditCategoryDialogVisible = false
                        )
                    }
                    addEditCategoryUseCase(event.category)
                    event.onSuccess?.invoke()
                }
            }

            is AddEditActivityEvent.OnCancelAddEditCategory -> {
                _uiState.update {
                    it.copy(
                        isAddEditCategoryDialogVisible = false
                    )
                }
            }

            is AddEditActivityEvent.OnDeleteCategoryClick -> {
                _uiState.update {
                    it.copy(
                        categoryToDelete = event.category,
                        isDeleteCategoryDialogVisible = true
                    )
                }
            }

            is AddEditActivityEvent.OnConfirmDeleteCategory -> {
                uiState.value.categoryToDelete?.let { category ->
                    viewModelScope.launch {
                        _uiState.update {
                            it.copy(
                                isDeleteCategoryDialogVisible = false
                            )
                        }
                        deleteCategoryUseCase(category, getDataSyncEnabledUseCase())
                        event.onSuccess?.invoke()
                    }
                }
            }

            is AddEditActivityEvent.OnCancelDeleteCategory -> {
                _uiState.update {
                    it.copy(
                        isDeleteCategoryDialogVisible = false
                    )
                }
            }

            is AddEditActivityEvent.OnRefreshActivity -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }

                    val activity = getActivityUseCase(event.activityId)

                    if (activity != null) {
                        _uiState.update {
                            it.copy(
                                title = activity.title,
                                date = getMillisFromDate(activity.date),
                                amount = activity.amount.let { amount ->
                                    if (amount < 0) amount * -1 else amount
                                }.toString(),
                                activityType = if (activity.amount < 0) {
                                    ActivityTypes.EXPENSE
                                } else {
                                    ActivityTypes.INCOME
                                },
                                selectedCategoryId = activity.categoryId
                            )
                        }
                    }

                    _uiState.update {
                        it.copy(
                            isLoading = false
                        )
                    }
                }
            }

            is AddEditActivityEvent.OnRefreshCategories -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }

                    if (isConnected.first()) {
                        val user = getUserUseCase()

                        if (user != null && getDataSyncEnabledUseCase()) {
                            syncCategoriesUseCase(user.userId)
                        }
                    }

                    val categories = getCategoriesUseCase()

                    _uiState.update {
                        it.copy(
                            categories = categories,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}