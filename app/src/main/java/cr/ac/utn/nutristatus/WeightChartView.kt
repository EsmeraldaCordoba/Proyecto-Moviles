package cr.ac.utn.nutristatus

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.DashPathEffect
import android.graphics.LinearGradient
import android.graphics.Paint
import android.graphics.Path
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.toColorInt

class WeightChartView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
) : View(context, attrs, defStyleAttr) {

    private val months = arrayOf("Ene", "Feb", "Mar", "Abr", "May", "Jun")
    private val weights = floatArrayOf(76.0f, 74.2f, 72.5f, 71.0f, 69.8f, 68.5f)

    private val bluePrimary = "#1565C0".toColorInt()

    private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = bluePrimary
        strokeWidth = 6f
        style = Paint.Style.STROKE
        strokeCap = Paint.Cap.ROUND
    }

    private val fillPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#E0E0E0".toColorInt()
        strokeWidth = 2f
        style = Paint.Style.STROKE
        pathEffect = DashPathEffect(floatArrayOf(10f, 10f), 0f)
    }

    private val pointOuterPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = bluePrimary
        style = Paint.Style.FILL
    }

    private val pointInnerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.WHITE
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#757575".toColorInt()
        textSize = 30f
        textAlign = Paint.Align.CENTER
    }

    private val gridTextPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = "#9E9E9E".toColorInt()
        textSize = 28f
        textAlign = Paint.Align.LEFT
    }

    private val labelPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = bluePrimary
        textSize = 32f
        textAlign = Paint.Align.CENTER
        isFakeBoldText = true
    }

    private val path = Path()
    private val fillPath = Path()

    private val pointX = FloatArray(weights.size)
    private val pointY = FloatArray(weights.size)

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val paddingTop = 60f
        val paddingBottom = 70f
        fillPaint.shader = LinearGradient(
            0f, paddingTop, 0f, h - paddingBottom,
            Color.argb(50, 21, 101, 192),
            Color.argb(0, 21, 101, 192),
            Shader.TileMode.CLAMP,
        )
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paddingLeft = 60f
        val paddingRight = 60f
        val paddingTop = 60f
        val paddingBottom = 70f

        val chartWidth = width - paddingLeft - paddingRight
        val chartHeight = height - paddingTop - paddingBottom

        if ((chartWidth <= 0f) || (chartHeight <= 0f)) return

        val minWeight = 65.0f
        val maxWeight = 78.0f
        val weightRange = maxWeight - minWeight


        val gridSteps = 3
        for (i in 0..gridSteps) {
            val y = paddingTop + ((chartHeight / gridSteps) * i)
            val gridValue = maxWeight - ((weightRange / gridSteps) * i)
            canvas.drawLine(paddingLeft, y, width - paddingRight, y, gridPaint)
            canvas.drawText("${gridValue.toInt()}kg", 10f, y + 10f, gridTextPaint)
        }


        val stepX = chartWidth / (weights.size - 1)
        for (i in weights.indices) {
            pointX[i] = paddingLeft + i * stepX
            val normalizedY = (weights[i] - minWeight) / weightRange
            pointY[i] = paddingTop + chartHeight * (1f - normalizedY)
        }

        path.reset()
        fillPath.reset()

        path.moveTo(pointX[0], pointY[0])
        fillPath.moveTo(pointX[0], height - paddingBottom)
        fillPath.lineTo(pointX[0], pointY[0])

        for (i in 0 until weights.size - 1) {
            val x1 = pointX[i]
            val y1 = pointY[i]
            val x2 = pointX[i + 1]
            val y2 = pointY[i + 1]

            val controlX1 = x1 + (x2 - x1) / 2f
            val controlX2 = x1 + (x2 - x1) / 2f

            path.cubicTo(controlX1, y1, controlX2, y2, x2, y2)
            fillPath.cubicTo(controlX1, y1, controlX2, y2, x2, y2)
        }

        fillPath.lineTo(pointX.last(), height - paddingBottom)
        fillPath.close()

        // Draw gradient area & line
        canvas.drawPath(fillPath, fillPaint)
        canvas.drawPath(path, linePaint)


        for (i in weights.indices) {
            val x = pointX[i]
            val y = pointY[i]


            canvas.drawCircle(x, y, 12f, pointOuterPaint)
            canvas.drawCircle(x, y, 7f, pointInnerPaint)


            canvas.drawText(months[i], x, height - 20f, textPaint)


            if (i == weights.size - 1) {
                canvas.drawText("${weights[i]}kg", x, y - 20f, labelPaint)
            }
        }
    }
}
