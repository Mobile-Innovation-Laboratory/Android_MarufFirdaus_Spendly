package dev.maruffirdaus.spendly.ui.home.component

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.patrykandpatrick.vico.compose.cartesian.CartesianChartHost
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberAxisLabelComponent
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberBottom
import com.patrykandpatrick.vico.compose.cartesian.axis.rememberStart
import com.patrykandpatrick.vico.compose.cartesian.layer.rememberColumnCartesianLayer
import com.patrykandpatrick.vico.compose.cartesian.rememberCartesianChart
import com.patrykandpatrick.vico.compose.cartesian.rememberVicoZoomState
import com.patrykandpatrick.vico.compose.common.component.rememberLineComponent
import com.patrykandpatrick.vico.compose.common.component.rememberTextComponent
import com.patrykandpatrick.vico.compose.common.component.shapeComponent
import com.patrykandpatrick.vico.compose.common.fill
import com.patrykandpatrick.vico.compose.common.insets
import com.patrykandpatrick.vico.compose.common.rememberHorizontalLegend
import com.patrykandpatrick.vico.core.cartesian.axis.HorizontalAxis
import com.patrykandpatrick.vico.core.cartesian.axis.VerticalAxis
import com.patrykandpatrick.vico.core.cartesian.data.CartesianChartModelProducer
import com.patrykandpatrick.vico.core.cartesian.data.columnSeries
import com.patrykandpatrick.vico.core.cartesian.layer.ColumnCartesianLayer
import com.patrykandpatrick.vico.core.common.LegendItem
import com.patrykandpatrick.vico.core.common.shape.CorneredShape
import dev.maruffirdaus.spendly.R
import dev.maruffirdaus.spendly.ui.home.constant.Months
import dev.maruffirdaus.spendly.ui.theme.LocalExtendedColorScheme
import dev.maruffirdaus.spendly.ui.theme.SpendlyTheme
import dev.maruffirdaus.spendly.ui.util.formatNumber

@Composable
fun GraphCard(
    currency: String,
    data: List<Long>,
    label: String,
    color: Color,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow,
            contentColor = MaterialTheme.colorScheme.onSurface
        )
    ) {
        val legendItemLabelComponent = rememberTextComponent(MaterialTheme.colorScheme.onSurface)
        val modelProducer = remember { CartesianChartModelProducer() }

        LaunchedEffect(data) {
            if (data.isNotEmpty()) {
                modelProducer.runTransaction {
                    columnSeries { series(data) }
                }
            }
        }

        CartesianChartHost(
            chart = rememberCartesianChart(
                rememberColumnCartesianLayer(
                    columnProvider = ColumnCartesianLayer.ColumnProvider.series(
                        rememberLineComponent(
                            fill = fill(color),
                            thickness = 16.dp,
                            shape = CorneredShape.rounded(
                                topLeftPercent = 50,
                                topRightPercent = 50
                            )
                        )
                    ),
                    columnCollectionSpacing = 18.dp
                ),
                startAxis = VerticalAxis.rememberStart(
                    label = rememberAxisLabelComponent(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    valueFormatter = { _, value, _ ->
                        buildString {
                            append(currency)
                            append(" ")
                            append(formatNumber(value))
                        }
                    }
                ),
                bottomAxis = HorizontalAxis.rememberBottom(
                    label = rememberAxisLabelComponent(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    valueFormatter = { _, value, _ ->
                        context.getString(Months.entries[value.toInt()].title)
                    }
                ),
                legend = rememberHorizontalLegend(
                    items = {
                        add(
                            LegendItem(
                                icon = shapeComponent(
                                    fill = fill(color),
                                    shape = CorneredShape.Pill
                                ),
                                labelComponent = legendItemLabelComponent,
                                label = label
                            )
                        )
                    },
                    padding = insets(top = 16.dp)
                )
            ),
            modelProducer = modelProducer,
            modifier = Modifier.padding(24.dp),
            zoomState = rememberVicoZoomState(zoomEnabled = false),
            placeholder = {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        )
    }
}

@Preview
@Composable
private fun GraphCardPreview() {
    SpendlyTheme {
        GraphCard(
            currency = "Rp",
            data = listOf(
                800000,
                600000,
                400000,
                750000,
                550000,
                650000,
                400000,
                800000,
                650000,
                750000,
                550000,
                650000
            ),
            label = stringResource(R.string.expense),
            color = LocalExtendedColorScheme.current.expense
        )
    }
}