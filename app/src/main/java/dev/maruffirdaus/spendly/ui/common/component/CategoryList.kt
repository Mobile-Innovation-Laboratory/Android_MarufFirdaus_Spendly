package dev.maruffirdaus.spendly.ui.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.common.model.Category
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun CategoryList(
    categories: List<Category>,
    selectedCategoryId: String?,
    onCategoryClick: (Category?) -> Unit,
    onAddEditCategoryClick: (Category?) -> Unit,
    onDeleteCategoryClick: (Category) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    LazyRow(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        items(items = categories, key = { it.categoryId }) { category ->
            Box {
                val isSelected =
                    category.categoryId == selectedCategoryId
                var isActionsVisible by remember { mutableStateOf(false) }

                CategoryChip(
                    title = {
                        Text(category.title)
                    },
                    selected = isSelected,
                    onClick = {
                        onCategoryClick(if (isSelected) null else category)
                    },
                    onLongClick = {
                        isActionsVisible = true
                    }
                )
                DropdownMenu(
                    expanded = isActionsVisible,
                    onDismissRequest = {
                        isActionsVisible = false
                    }
                ) {
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.edit))
                        },
                        onClick = {
                            onAddEditCategoryClick(category)
                            isActionsVisible = false
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_edit),
                                contentDescription = null
                            )
                        }
                    )
                    DropdownMenuItem(
                        text = {
                            Text(stringResource(R.string.delete))
                        },
                        onClick = {
                            onDeleteCategoryClick(category)
                            isActionsVisible = false
                        },
                        leadingIcon = {
                            Icon(
                                painter = painterResource(R.drawable.ic_delete),
                                contentDescription = null
                            )
                        }
                    )
                }
            }
        }

        item {
            ActionChip(
                onClick = {
                    onAddEditCategoryClick(null)
                }
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_add),
                    contentDescription = stringResource(R.string.add_category)
                )
            }
        }
    }
}

@Preview
@Composable
private fun CategoryListPreview() {
    SpendlyTheme {
        CategoryList(
            categories = listOf(
                Category(
                    title = "Food",
                    categoryId = ""
                ),
                Category(
                    title = "Electronic",
                    categoryId = ""
                )
            ),
            selectedCategoryId = null,
            onCategoryClick = {},
            onAddEditCategoryClick = {},
            onDeleteCategoryClick = {},
        )
    }
}