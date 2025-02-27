package dev.maruffirdaus.spendly.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maruffirdaus.spendly.domain.usecase.activity.GetActivitiesUseCase
import dev.maruffirdaus.spendly.domain.usecase.activity.SyncActivitiesUseCase
import dev.maruffirdaus.spendly.domain.usecase.auth.GetUserUseCase
import dev.maruffirdaus.spendly.domain.usecase.category.SyncCategoriesUseCase
import dev.maruffirdaus.spendly.domain.usecase.preference.GetDataSyncEnabledUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.DeleteWalletUseCase
import dev.maruffirdaus.spendly.domain.usecase.wallet.SyncWalletsUseCase
import dev.maruffirdaus.spendly.ui.home.constant.Months
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getUserUseCase: GetUserUseCase,
    private val getDataSyncEnabledUseCase: GetDataSyncEnabledUseCase,
    private val deleteWalletUseCase: DeleteWalletUseCase,
    private val syncWalletsUseCase: SyncWalletsUseCase,
    private val syncCategoriesUseCase: SyncCategoriesUseCase,
    private val getActivitiesUseCase: GetActivitiesUseCase,
    private val syncActivitiesUseCase: SyncActivitiesUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.OnScrollPositionChange -> {
                _uiState.update {
                    it.copy(
                        scrollPosition = event.scrollPosition
                    )
                }
            }

            is HomeEvent.OnDeleteWalletClick -> {
                _uiState.update {
                    it.copy(
                        walletToDelete = event.wallet,
                        isDeleteWalletDialogVisible = true
                    )
                }
            }

            is HomeEvent.OnConfirmDeleteWallet -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isDeleteWalletDialogVisible = false
                        )
                    }
                    uiState.value.walletToDelete?.let { wallet ->
                        deleteWalletUseCase(wallet, getDataSyncEnabledUseCase())
                    }
                    event.onSuccess?.invoke()
                    _uiState.update {
                        it.copy(
                            walletToDelete = null
                        )
                    }
                }
            }

            is HomeEvent.OnCancelDeleteWallet -> {
                _uiState.update {
                    it.copy(
                        walletToDelete = null,
                        isDeleteWalletDialogVisible = false
                    )
                }
            }

            is HomeEvent.OnRefreshGraphData -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }

                    val user = getUserUseCase()

                    if (user != null && getDataSyncEnabledUseCase()) {
                        syncWalletsUseCase(user.userId)
                        syncCategoriesUseCase(user.userId)
                        syncActivitiesUseCase(user.userId)
                    }

                    val incomeData: MutableList<Long> = emptyList<Long>().toMutableList()
                    val expenseData: MutableList<Long> = emptyList<Long>().toMutableList()

                    Months.entries.forEach { month ->
                        incomeData += getActivitiesUseCase(
                            walletId = event.walletId,
                            categoryId = null,
                            year = event.year,
                            month = month.id
                        ).filter { 0 < it.amount }.sumOf { it.amount }

                        expenseData += getActivitiesUseCase(
                            walletId = event.walletId,
                            categoryId = null,
                            year = event.year,
                            month = month.id
                        ).filter { it.amount < 0 }.sumOf { it.amount * -1 }
                    }

                    _uiState.update {
                        it.copy(
                            incomeData = incomeData,
                            expenseData = expenseData,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}