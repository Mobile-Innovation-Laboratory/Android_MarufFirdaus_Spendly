package dev.maruffirdaus.spendly.ui.deals.component

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.staggeredgrid.LazyStaggeredGridState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.common.constant.DealsSources
import dev.maruffirdaus.spendly.common.model.Deals
import dev.maruffirdaus.spendly.ui.common.component.MessageCard
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme

@Composable
fun DealGrid(
    dealsSource: DealsSources,
    deals: Deals,
    onNextClick: () -> Unit,
    onPrevClick: () -> Unit,
    onRefreshClick: () -> Unit,
    modifier: Modifier = Modifier,
    state: LazyStaggeredGridState = rememberLazyStaggeredGridState(),
    error: Boolean = false,
    minItemSize: Dp = 320.dp,
    contentPadding: PaddingValues = PaddingValues(0.dp),
    itemSpacing: Dp = 16.dp
) {
    val context = LocalContext.current

    LazyVerticalStaggeredGrid(
        columns = StaggeredGridCells.Adaptive(minItemSize),
        modifier = modifier,
        state = state,
        contentPadding = contentPadding,
        verticalItemSpacing = itemSpacing,
        horizontalArrangement = Arrangement.spacedBy(itemSpacing)
    ) {
        if (deals.items.isNotEmpty()) {
            items(deals.items) { deal ->
                DealCard(
                    deal = deal,
                    onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(deal.url)))
                    }
                )
            }

            item(span = StaggeredGridItemSpan.FullLine) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    dealsSource.providerName?.let { nameRes ->
                        val annotatedString = buildAnnotatedString {
                            val name = stringResource(nameRes)
                            val message = stringResource(R.string.attribution_message, name)

                            append(message)

                            dealsSource.providerUrl?.let { url ->
                                val nameStart = message.indexOf(name)
                                val nameEnd = nameStart + name.length

                                addStyle(
                                    style = SpanStyle(
                                        textDecoration = TextDecoration.Underline
                                    ),
                                    start = nameStart,
                                    end = nameEnd
                                )
                                addStringAnnotation(
                                    tag = "URL",
                                    annotation = url,
                                    start = nameStart,
                                    end = nameEnd
                                )
                            }
                        }

                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = annotatedString,
                            modifier = Modifier
                                .widthIn(max = 600.dp)
                                .fillMaxWidth()
                                .padding(horizontal = 28.dp)
                                .pointerInput(Unit) {
                                    detectTapGestures {
                                        annotatedString.getStringAnnotations(
                                            tag = "URL",
                                            start = 0,
                                            end = annotatedString.length
                                        ).firstOrNull()?.let { annotation ->
                                            context.startActivity(
                                                Intent(
                                                    Intent.ACTION_VIEW,
                                                    Uri.parse(annotation.item)
                                                )
                                            )
                                        }
                                    }
                                },
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            textAlign = TextAlign.Center,
                            overflow = TextOverflow.Ellipsis,
                            maxLines = 1,
                            style = MaterialTheme.typography.labelSmall
                        )
                        Spacer(Modifier.height(24.dp))
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(
                        modifier = Modifier
                            .widthIn(max = 600.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp)
                    ) {
                        OutlinedButton(
                            onClick = onPrevClick,
                            modifier = Modifier.weight(1f),
                            enabled = 0 < deals.nextOffset - deals.items.size
                        ) {
                            Text(stringResource(R.string.prev))
                        }
                        Spacer(Modifier.width(8.dp))
                        Button(
                            onClick = onNextClick,
                            modifier = Modifier.weight(1f),
                            enabled = deals.hasMore
                        ) {
                            Text(stringResource(R.string.next))
                        }
                    }
                }
            }
        } else {
            item(span = StaggeredGridItemSpan.FullLine) {
                Box(
                    contentAlignment = Alignment.Center
                ) {
                    MessageCard(
                        text = {
                            Text(
                                if (error) {
                                    stringResource(R.string.fetch_deals_error_message)
                                } else {
                                    stringResource(R.string.empty_deal_list_message)
                                }
                            )
                        },
                        action = {
                            OutlinedButton(
                                onClick = onRefreshClick,
                                modifier = Modifier
                                    .widthIn(max = 372.dp)
                                    .fillMaxWidth()
                            ) {
                                Text(
                                    if (error) {
                                        stringResource(R.string.try_again)
                                    } else {
                                        stringResource(R.string.refresh)
                                    }
                                )
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
}

@Preview
@Composable
private fun DealGridPreview() {
    SpendlyTheme {
        DealGrid(
            dealsSource = DealsSources.STEAM,
            deals = Deals(),
            onNextClick = {},
            onPrevClick = {},
            onRefreshClick = {},
            modifier = Modifier.fillMaxSize(),
        )
    }
}