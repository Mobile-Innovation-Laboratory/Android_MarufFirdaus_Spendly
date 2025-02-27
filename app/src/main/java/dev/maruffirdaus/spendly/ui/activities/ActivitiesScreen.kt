package dev.maruffirdaus.spendly.ui.activities

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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.PrimaryScrollableTabRow
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.common.model.Wallet
import dev.maruffirdaus.spendly.ui.AddEditActivity
import dev.maruffirdaus.spendly.ui.activities.component.ActivitiesTopAppBar
import dev.maruffirdaus.spendly.ui.activities.component.ActivityList
import dev.maruffirdaus.spendly.ui.activities.constant.MonthsFilter
import dev.maruffirdaus.spendly.ui.common.SharedEvent
import dev.maruffirdaus.spendly.ui.common.SharedUiState
import dev.maruffirdaus.spendly.ui.common.component.AddEditCategoryDialog
import dev.maruffirdaus.spendly.ui.common.component.BottomGradient
import dev.maruffirdaus.spendly.ui.common.component.CategoryList
import dev.maruffirdaus.spendly.ui.common.component.TopGradient
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivitiesScreen(
    sharedUiState: SharedUiState,
    uiState: ActivitiesUiState,
    onSharedEvent: (SharedEvent) -> Unit,
    onEvent: (ActivitiesEvent) -> Unit,
    onMenuClick: (() -> Unit)?,
    onSelectYearClick: () -> Unit,
    onRefreshWallets: () -> Unit,
    modifier: Modifier = Modifier,
    compactScreen: Boolean = true,
    navController: NavController = rememberNavController()
) {
    var isFirstComposition by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        onEvent(ActivitiesEvent.OnRefreshCategories)
    }

    LaunchedEffect(
        sharedUiState.selectedWallet,
        uiState.selectedCategoryId,
        sharedUiState.selectedYear,
        uiState.selectedMonthFilter
    ) {
        sharedUiState.selectedWallet?.walletId?.let { walletId ->
            onEvent(
                ActivitiesEvent.OnRefreshActivities(
                    walletId = walletId,
                    year = sharedUiState.selectedYear
                )
            )
        }
    }

    LaunchedEffect(uiState.categories) {
        val categoryIds = uiState.categories.map { it.categoryId }.toSet()

        if (uiState.selectedCategoryId !in categoryIds) {
            onEvent(ActivitiesEvent.OnSelectedCategoryIdChange(null))
        }
    }

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val pagerState =
            rememberPagerState(initialPage = uiState.selectedTabIndex) { MonthsFilter.entries.size }
        val categoryListState = rememberLazyListState(
            initialFirstVisibleItemIndex = uiState.categoryListPosition.first,
            initialFirstVisibleItemScrollOffset = uiState.categoryListPosition.second
        )
        val activityListState = rememberLazyListState(
            initialFirstVisibleItemIndex = uiState.activityListPosition.first,
            initialFirstVisibleItemScrollOffset = uiState.activityListPosition.second
        )

        LaunchedEffect(pagerState.currentPage) {
            onEvent(ActivitiesEvent.OnSelectedTabIndexChange(pagerState.currentPage))
        }

        LaunchedEffect(uiState.selectedTabIndex) {
            if (!isFirstComposition) {
                activityListState.scrollToItem(0, 0)
            }

            isFirstComposition = false
        }

        LaunchedEffect(categoryListState) {
            snapshotFlow {
                categoryListState.firstVisibleItemIndex to categoryListState.firstVisibleItemScrollOffset
            }.collect { (index, offset) ->
                onEvent(
                    ActivitiesEvent.OnCategoryListPositionChange(index, offset)
                )
            }
        }

        LaunchedEffect(activityListState) {
            snapshotFlow {
                activityListState.firstVisibleItemIndex to activityListState.firstVisibleItemScrollOffset
            }.collect { (index, offset) ->
                onEvent(
                    ActivitiesEvent.OnActivityListPositionChange(index, offset)
                )
            }
        }

        ActivitiesTopAppBar(
            selectedYear = sharedUiState.selectedYear,
            onMenuClick = onMenuClick,
            onYearPickerClick = {
                onSelectYearClick()
            }
        )
        PrimaryScrollableTabRow(
            selectedTabIndex = pagerState.currentPage,
            divider = {}
        ) {
            MonthsFilter.entries.forEachIndexed { index, monthFilter ->
                Tab(
                    selected = index == pagerState.currentPage,
                    onClick = {
                        scope.launch {
                            pagerState.animateScrollToPage(index)
                        }
                    },
                    text = {
                        Text(stringResource(monthFilter.title))
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
                    .zIndex(2f)
            )
            this@Column.AnimatedVisibility(
                visible = uiState.isCategoryListVisible,
                modifier = Modifier
                    .height(64.dp)
                    .zIndex(if (uiState.isCategoryListVisible) 1f else -1f)
            ) {
                CategoryList(
                    categories = uiState.categories,
                    selectedCategoryId = uiState.selectedCategoryId,
                    onCategoryClick = { category ->
                        onEvent(
                            ActivitiesEvent.OnSelectedCategoryIdChange(
                                category?.categoryId
                            )
                        )
                    },
                    onAddEditCategoryClick = { category ->
                        onEvent(
                            ActivitiesEvent.OnAddEditCategoryClick(
                                category
                            )
                        )
                    },
                    onDeleteCategoryClick = { category ->
                        onEvent(ActivitiesEvent.OnDeleteCategoryClick(category))
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(32.dp),
                    state = categoryListState,
                    contentPadding = PaddingValues(16.dp)
                )
            }
            this@Column.AnimatedVisibility(
                visible = uiState.isActivitiesLoading,
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
                    visible = isCurrentPageVisible && !uiState.isActivitiesLoading,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    sharedUiState.selectedWallet?.let { wallet ->
                        ActivityList(
                            currency = wallet.currency,
                            activities = uiState.activities,
                            onAddEditActivityClick = { activity ->
                                navController.navigate(
                                    AddEditActivity(
                                        walletId = wallet.walletId,
                                        currency = wallet.currency,
                                        activityId = activity?.activityId
                                    )
                                )
                            },
                            onDeleteActivityClick = { activity ->
                                onEvent(
                                    ActivitiesEvent.OnDeleteActivityClick(
                                        activity
                                    )
                                )
                            },
                            modifier = Modifier.fillMaxSize(),
                            state = activityListState,
                            contentPadding = PaddingValues(
                                start = 16.dp,
                                top = 64.dp,
                                end = 16.dp,
                                bottom = 88.dp + WindowInsets.navigationBars.asPaddingValues()
                                    .calculateBottomPadding()
                            )
                        )
                    }
                }
            }
            BottomGradient(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(16.dp)
                    .align(Alignment.BottomStart)
                    .zIndex(2f)
            )
        }
    }

    if (uiState.isAddEditCategoryDialogVisible) {
        AddEditCategoryDialog(
            onDismissRequest = {
                onEvent(ActivitiesEvent.OnCancelAddEditCategory)
            },
            onSave = { category ->
                onEvent(
                    ActivitiesEvent.OnSaveCategory(
                        category = category,
                        onSuccess = {
                            onEvent(ActivitiesEvent.OnRefreshCategories)
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
                onEvent(ActivitiesEvent.OnCancelDeleteCategory)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(
                            ActivitiesEvent.OnConfirmDeleteCategory(
                                onSuccess = {
                                    onEvent(ActivitiesEvent.OnRefreshCategories)
                                    onEvent(ActivitiesEvent.OnSelectedCategoryIdChange(null))
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
                        onEvent(ActivitiesEvent.OnCancelDeleteCategory)
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

    if (uiState.isDeleteActivityDialogVisible) {
        AlertDialog(
            onDismissRequest = {
                onEvent(ActivitiesEvent.OnCancelDeleteActivity)
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onEvent(
                            ActivitiesEvent.OnConfirmDeleteActivity(
                                onSuccess = {
                                    sharedUiState.selectedWallet?.walletId?.let { walletId ->
                                        onEvent(
                                            ActivitiesEvent.OnRefreshActivities(
                                                walletId = walletId,
                                                year = sharedUiState.selectedYear
                                            )
                                        )
                                        onRefreshWallets()
                                        onSharedEvent(SharedEvent.OnRefreshSelectedWallet)
                                    }
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
                        onEvent(ActivitiesEvent.OnCancelDeleteActivity)
                    }
                ) {
                    Text(stringResource(R.string.cancel))
                }
            },
            title = {
                Text(stringResource(R.string.delete_activity_title))
            },
            text = {
                Text(stringResource(R.string.delete_activity_message))
            }
        )
    }
}

@Preview
@Composable
private fun ActivitiesScreenPreview() {
    SpendlyTheme {
        Surface {
            ActivitiesScreen(
                sharedUiState = SharedUiState(
                    selectedWallet = Wallet(
                        title = "Dana",
                        currency = "Rp",
                        balance = 1500000,
                        walletId = ""
                    )
                ),
                uiState = ActivitiesUiState(
                    selectedTabIndex = 0,
                    activities = listOf(
                        Activity(
                            walletId = "",
                            title = "Pay YouTube Premium",
                            date = "2024-04-04",
                            amount = -59000,
                            activityId = ""
                        )
                    )
                ),
                onSharedEvent = {},
                onEvent = {},
                onMenuClick = {},
                onSelectYearClick = {},
                onRefreshWallets = {}
            )
        }
    }
}

@Preview
@Composable
private fun ActivitiesScreenEmptyPreview() {
    SpendlyTheme {
        Surface {
            ActivitiesScreen(
                sharedUiState = SharedUiState(),
                uiState = ActivitiesUiState(),
                onSharedEvent = {},
                onEvent = {},
                onMenuClick = {},
                onSelectYearClick = {},
                onRefreshWallets = {}
            )
        }
    }
}