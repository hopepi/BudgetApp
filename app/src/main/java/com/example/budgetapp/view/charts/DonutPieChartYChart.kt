package com.example.budgetapp.view.charts

import android.app.slice.Slice
import android.graphics.Typeface
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.DonutPieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData

@Composable
fun DonutPie(
    modifier: Modifier = Modifier,
    donutChartData: PieChartData,
    sumUnit : String
) {

    val donutChartConfig = PieChartConfig(
        labelVisible = true,
        strokeWidth = 100f,
        labelColor = Color.Black,
        activeSliceAlpha = .9f,
        isEllipsizeEnabled = true,
        backgroundColor = Color.Transparent,
        labelTypeface = Typeface.defaultFromStyle(Typeface.BOLD),
        isAnimationEnable = true,
        chartPadding = 25,
        labelFontSize = 16.sp,
        sumUnit = sumUnit,
        labelColorType = PieChartConfig.LabelColorType.SLICE_COLOR,
        labelType = PieChartConfig.LabelType.VALUE
    )

    DonutPieChart(
        modifier = Modifier
            .background(color = Color(0xFFB2DFDB))
            .padding(5.dp)
            .height(200.dp),
        donutChartData,
        donutChartConfig
    )
}