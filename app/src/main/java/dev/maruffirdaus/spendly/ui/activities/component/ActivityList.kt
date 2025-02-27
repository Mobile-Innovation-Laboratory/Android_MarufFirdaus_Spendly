package dev.maruffirdaus.spendly.ui.activities.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.common.model.Activity
import dev.maruffirdaus.spendly.ui.common.component.MessageCard
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun ActivityList(
    currency: String,
    activities: List<Activity>,
    onAddEditActivityClick: (Activity?) -> Unit,
    onDeleteActivityClick: (Activity) -> Unit,
    modifier: Modifier = Modifier,
    state: LazyListState = rememberLazyListState(),
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemSpacing: Dp = 16.dp
) {
    LazyColumn(
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(itemSpacing),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (activities.isNotEmpty()) {
            items(activities) { activity ->
                Box(
                    modifier = Modifier.widthIn(max = 600.dp)
                ) {
                    var isActionsVisible by remember { mutableStateOf(false) }

                    ActivityCard(
                        currency = currency,
                        activity = activity,
                        onClick = {
                            isActionsVisible = !isActionsVisible
                        },
                        modifier = Modifier.fillMaxWidth()
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
                                onAddEditActivityClick(activity)
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
                                onDeleteActivityClick(activity)
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
        } else {
            item {
                MessageCard(
                    text = {
                        Text(stringResource(R.string.empty_activity_list_message))
                    },
                    action = {
                        OutlinedButton(
                            onClick = {
                                onAddEditActivityClick(null)
                            },
                            modifier = Modifier
                                .widthIn(max = 372.dp)
                                .fillMaxWidth()
                        ) {
                            Text(stringResource(R.string.add_activity))
                        }
                    },
                    modifier = Modifier
                        .widthIn(max = 600.dp)
                        .fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
private fun ActivityListPreview() {
    SpendlyTheme {
        ActivityList(
            currency = "Rp",
            activities = listOf(
                Activity(
                    walletId = "",
                    title = "Pay YouTube Premium",
                    date = "2024-04-04",
                    amount = -59000,
                    activityId = ""
                )
            ),
            onAddEditActivityClick = {},
            onDeleteActivityClick = {}
        )
    }
}