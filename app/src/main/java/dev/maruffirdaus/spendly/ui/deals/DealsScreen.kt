package dev.maruffirdaus.spendly.ui.deals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import dev.maruffirdaus.spendly.common.constant.DealsSources
import dev.maruffirdaus.spendly.ui.common.component.BottomGradient
import dev.maruffirdaus.spendly.ui.common.component.TopGradient
import dev.maruffirdaus.spendly.ui.deals.component.DealGrid
import dev.maruffirdaus.spendly.ui.deals.component.DealsTopAppBar
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DealsScreen(
    uiState: DealsUiState,
    onEvent: (DealsEvent) -> Unit,
    onMenuClick: (() -> Unit)?,
    modifier: Modifier = Modifier,
    country: String = "US",
    compactScreen: Boolean = true
) {
    var isFirstComposition by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.selectedDealsSource) {
        onEvent(
            DealsEvent.OnRefreshDeals(
                source = uiState.selectedDealsSource,
                country = country,
                offset = if (isFirstComposition) {
                    uiState.deals.nextOffset - uiState.deals.items.size
                } else {
                    0
                }
            )
        )
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val pagerState =
            rememberPagerState(initialPage = uiState.selectedTabIndex) { DealsSources.entries.size }
        val dealGridState = rememberLazyStaggeredGridState(
            initialFirstVisibleItemIndex = uiState.dealGridPosition.first,
            initialFirstVisibleItemScrollOffset = uiState.dealGridPosition.second
        )

        LaunchedEffect(pagerState.currentPage) {
            onEvent(DealsEvent.OnSelectedTabIndexChange(pagerState.currentPage))
        }

        LaunchedEffect(dealGridState) {
            snapshotFlow {
                dealGridState.firstVisibleItemIndex to dealGridState.firstVisibleItemScrollOffset
            }.collect { (index, offset) ->
                onEvent(DealsEvent.OnDealGridPositionChange(index, offset))
            }
        }

        LaunchedEffect(uiState.deals) {
            if (!isFirstComposition) {
                dealGridState.scrollToItem(0, 0)
            }

            isFirstComposition = false
        }

        DealsTopAppBar(
            onMenuClick = onMenuClick
        )
        PrimaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            containerColor = Color.Transparent,
            divider = {}
        ) {
            DealsSources.entries.forEachIndexed { index, dealsSource ->
                Tab(
                    selected = index == pagerState.currentPage,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(stringResource(dealsSource.title))
                    },
                    unselectedContentColor = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
        Box(
            modifier = Modifier.weight(1f)
        ) {
            TopGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .zIndex(1f)
            )
            this@Column.AnimatedVisibility(
                visible = uiState.isLoading,
                modifier = Modifier
                    .let {
                        if (compactScreen) {
                            it
                        } else {
                            it.padding(
                                bottom = WindowInsets.navigationBars.asPaddingValues()
                                    .calculateBottomPadding()
                            )
                        }
                    }
                    .align(Alignment.Center),
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                CircularProgressIndicator()
            }
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxSize()
            ) { page ->
                val isCurrentPageVisible = page == uiState.selectedTabIndex

                this@Column.AnimatedVisibility(
                    visible = isCurrentPageVisible && !uiState.isLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    DealGrid(
                        dealsSource = uiState.selectedDealsSource,
                        deals = uiState.deals,
                        onNextClick = {
                            onEvent(
                                DealsEvent.OnRefreshDeals(
                                    source = uiState.selectedDealsSource,
                                    country = country,
                                    offset = uiState.deals.nextOffset
                                )
                            )
                        },
                        onPrevClick = {
                            onEvent(
                                DealsEvent.OnRefreshDeals(
                                    source = uiState.selectedDealsSource,
                                    country = country,
                                    offset = uiState.deals.nextOffset - uiState.deals.items.size * 2
                                )
                            )
                        },
                        onRefreshClick = {
                            onEvent(
                                DealsEvent.OnRefreshDeals(
                                    source = uiState.selectedDealsSource,
                                    country = country,
                                    offset = uiState.deals.nextOffset - uiState.deals.items.size
                                )
                            )
                        },
                        modifier = Modifier.fillMaxSize(),
                        state = dealGridState,
                        error = uiState.isError,
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = 16.dp + WindowInsets.navigationBars.asPaddingValues()
                                .calculateBottomPadding()
                        )
                    )
                }
            }
            BottomGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .align(Alignment.BottomStart)
                    .zIndex(1f)
            )
        }
    }
}

@Preview
@Composable
private fun DealsScreenPreview() {
    SpendlyTheme {
        Surface {
            DealsScreen(
                uiState = DealsUiState(),
                onEvent = {},
                onMenuClick = {}
            )
        }
    }
}