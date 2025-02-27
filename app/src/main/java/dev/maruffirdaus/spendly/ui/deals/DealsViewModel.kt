package dev.maruffirdaus.spendly.ui.deals

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.maruffirdaus.spendly.common.constant.DealsSources
import dev.maruffirdaus.spendly.common.model.Deals
import dev.maruffirdaus.spendly.domain.usecase.deal.GetDealsUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DealsViewModel @Inject constructor(
    private val getDealsUseCase: GetDealsUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(DealsUiState())
    val uiState = _uiState.asStateFlow()

    fun onEvent(event: DealsEvent) {
        when (event) {
            is DealsEvent.OnSelectedTabIndexChange -> {
                _uiState.update {
                    it.copy(
                        selectedTabIndex = event.selectedTabIndex,
                        selectedDealsSource = DealsSources.entries[event.selectedTabIndex]
                    )
                }
            }

            is DealsEvent.OnDealGridPositionChange -> {
                _uiState.update {
                    it.copy(
                        dealGridPosition = Pair(event.index, event.offset)
                    )
                }
            }

            is DealsEvent.OnRefreshDeals -> {
                viewModelScope.launch {
                    _uiState.update {
                        it.copy(
                            isLoading = true
                        )
                    }

                    getDealsUseCase(
                        source = event.source,
                        country = event.country,
                        offset = event.offset ?: uiState.value.deals.nextOffset,
                    ).fold(
                        onSuccess = { deals ->
                            _uiState.update {
                                it.copy(
                                    deals = deals,
                                    isLoading = false,
                                    isError = false
                                )
                            }
                        },
                        onFailure = {
                            _uiState.update {
                                it.copy(
                                    deals = Deals(),
                                    isLoading = false,
                                    isError = true
                                )
                            }
                        }
                    )
                }
            }
        }
    }
}