package com.example.budgetapp.view.charts

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.round
import kotlin.math.roundToInt

@Composable
fun LineChart(
    modifier: Modifier = Modifier,
    data : List<Pair<Int,Double>> = emptyList()

) {
    val spacing = 100f
    val graphColor = Color.Cyan
    val transparentGraphColor = remember {
        graphColor.copy(alpha = 0.5f)
    }

    val upperValue = remember {
        (data.maxOfOrNull { it.second }?.plus(1))?.roundToInt() ?: 0
    }

    val lowerValue = remember {
        (data.minOfOrNull { it.second }?.toInt() ?: 0)
    }

    val density = LocalDensity.current

    val textPoint = remember(density){
        Paint().apply {
            color = android.graphics.Color.WHITE
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    
    Canvas(modifier = modifier) {
        val spacePerHour = (size.width - spacing) / data.size
        (data.indices step 1).forEach{
            val hour = data[it].first
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    hour.toString(),
                    spacing + it * spacePerHour,
                    size.height,
                    textPoint
                )
            }
        }

        val priceStep = (upperValue-lowerValue) / 7f
        (0..6).forEach{
            drawContext.canvas.nativeCanvas.apply {
                drawText(
                    round(lowerValue + priceStep * it).toString(),
                    45f,
                    size.height - spacing - it * size.height / 7f +20f,
                    textPoint
                )
            }
        }

        val strokePath = Path().apply {
            val height = size.height
            data.indices.forEach {
                val info = data[it]
                val ratio = (info.second - lowerValue) / (upperValue - lowerValue)

                val x1 = spacing + it * spacePerHour
                val y1 = height -spacing -(ratio * height).toFloat()

                if (it == 0) moveTo(x1 ,y1)
                lineTo(x1,y1)
            }
        }

        drawPath(
            path = strokePath,
            color = graphColor,
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round
            )
        )
        val fillPath = android.graphics.Path(strokePath.asAndroidPath()).asComposePath().apply {
            lineTo(size.width - spacePerHour,size.height -spacing)
            lineTo(spacing,size.height-spacing)
            close()
        }

        drawPath(
            path = fillPath,
            brush = Brush.verticalGradient(
                colors = listOf(transparentGraphColor,Color.Transparent), endY = size.height-spacing)
        )

    }
}