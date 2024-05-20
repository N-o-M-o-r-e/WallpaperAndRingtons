package com.project.tathanhson.myapplication

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.BlurMaskFilter
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PathEffect
import android.graphics.PointF
import android.graphics.RectF
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.view.MotionEvent
import android.view.View
import com.project.tathanhson.wallpaperandringtons.R
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("DrawAllocation")
class OnBroadIndicatorView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    companion object {
        private const val BACKGROUND_SHADOW_RADIUS = 40f
        private const val BACKGROUND_ROUNDED_DP = 28f
        private const val BACKGROUND_MARGIN_DP = 20f
        private const val CIRCLE_SIZE_DP = 42f
        private const val CIRCLE_MARGIN_HORIZONTAL = 44f
        private const val CIRCLE_MARGIN_BOTTOM = 54f
        private const val CIRCLE_SPACE_SIZE_DP = 10f
    }

    private lateinit var backgroundRect: RectF
    private var backGroundPath: Path = Path()
    private val backgroundPaint by lazy {
        Paint().apply {
            this.color = Color.BLACK
            this.style = Paint.Style.FILL
            this.setPathEffect(PathEffect())
            this.isAntiAlias = true
        }
    }
    private val backgroundShadowPaint by lazy {
        Paint().apply {
            this.color = Color.parseColor("#00AF5B")
            this.setMaskFilter(BlurMaskFilter(BACKGROUND_SHADOW_RADIUS, BlurMaskFilter.Blur.OUTER))
            this.alpha = (0.2 * 255).toInt()
        }
    }

    private val circleSize = context.dpToPx(CIRCLE_SIZE_DP)
    private lateinit var circleRect: RectF
    private val circlePaint by lazy {
        Paint().apply {
            this.color = Color.BLUE
        }
    }

    private var stepCount = 3
    private var touch: PointF? = null
    private var moveToNearStepAnimate: ValueAnimator? = null

    fun setSepCount(count: Int) {
        this.stepCount = count
        invalidate()
    }

    fun setCurrentSepCount(count: Int, positionOffset: Float) {
        val start = startDrag()
        val stepDistance = stepDistance()
        val newCenter = start + count * stepDistance + positionOffset * stepDistance
        val newLeft = newCenter - circleRect.width() / 2
        val newRight = newLeft + circleRect.width()
        circleRect.set(
            newLeft,
            circleRect.top,
            newRight,
            circleRect.bottom
        )
        invalidate()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        backgroundRect = RectF(0f, 0f, widthSize.toFloat(), heightSize.toFloat())

        val circleLeft = context.dpToPx(CIRCLE_MARGIN_HORIZONTAL) + backgroundMargin
        val circleTop =
            heightSize - context.dpToPx(CIRCLE_MARGIN_BOTTOM) - circleSize - backgroundMargin

        circleRect = RectF(
            circleLeft,
            circleTop,
            circleLeft + circleSize,
            circleTop + circleSize
        )
    }

    fun Context.dpToPx(dp: Float): Float {
        return dp * ((this.resources?.displayMetrics?.densityDpi?.toFloat()
            ?: 0f) / DisplayMetrics.DENSITY_DEFAULT)
    }

    fun Context.pxToDp(px: Float): Float {
        return px / ((this.resources?.displayMetrics?.densityDpi?.toFloat()
            ?: 0f) / DisplayMetrics.DENSITY_DEFAULT)
    }

    private val backgroundMargin = context.dpToPx(BACKGROUND_MARGIN_DP)
    private val backgroundRounded = context.dpToPx(BACKGROUND_ROUNDED_DP)
    private val circleRounded = context.dpToPx(20f)

    private fun Path.moveTo(point: PointF) {
        this.moveTo(point.x, point.y)
    }

    private fun Path.lineTo(point: PointF) {
        this.lineTo(point.x, point.y)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val spaceSize = context.dpToPx(CIRCLE_SPACE_SIZE_DP)
        val overCircleRect = circleRect.overSizeRect(spaceSize)
        backGroundPath.apply {
            val WIDTH = backgroundRect.width()
            val HEIGHT = backgroundRect.height()
            this.reset()

            this.moveTo(backgroundMargin, backgroundMargin)

            this.arcTo(
                RectF(
                    backgroundMargin,
                    backgroundMargin,
                    backgroundMargin + backgroundRounded,
                    backgroundMargin + backgroundRounded
                ), 180f, 90f
            )

            this.lineTo(
                WIDTH - backgroundMargin - backgroundRounded,
                backgroundMargin
            )

            this.arcTo(
                RectF(
                    WIDTH - backgroundMargin - backgroundRounded,
                    backgroundMargin,
                    WIDTH - backgroundMargin,
                    backgroundMargin + backgroundRounded
                ), 270f, 90f
            )

            val overCircleXCenter = overCircleRect.centerX()
            val overCircleYCenter = overCircleRect.centerY()
            val overCircleRadius = overCircleRect.width() / 2
            val circleRadius = circleRect.width() / 2
            //
//         val spaceToRight = getMaxCircleLeft() + circleRect.width() - overCircleRect.right + spaceSize
//         val scaleToRight = ((overCircleRect.width() - spaceToRight).coerceAtLeast(0f) / overCircleRect.width())
//            .let {
//               when {
//                  it > 0.98 -> 1f
//                  it < 0.02 -> 0f
//                  else -> it
//               }
//            }
            val spaceToRight = (circleRect.centerX() - backgroundRect.centerX()).coerceAtLeast(0f)
            val scaleToRight = (spaceToRight / stepDistance())
                .let {
                    when {
                        it > 0.98 -> 1f
                        it < 0.02 -> 0f
                        else -> it
                    }
                }

//         val spaceToLeft = overCircleRect.left - getMinCircleLeft() + spaceSize
//         val scaleToLeft = ((overCircleRect.width() - spaceToLeft)
//            .coerceAtLeast(0f) / overCircleRect.width())
//            .let {
//               when {
//                  it > 0.98 -> 1f
//                  it < 0.02 -> 0f
//                  else -> it
//               }
//            }
            val spaceToLeft = (backgroundRect.centerX() - circleRect.centerX()).coerceAtLeast(0f)
            val scaleToLeft = (spaceToLeft / stepDistance())
                .let {
                    when {
                        it > 0.98 -> 1f
                        it < 0.02 -> 0f
                        else -> it
                    }
                }

            val point1 = PointF(0f, 0f)
            val point2 = PointF(0f, 0f)
            val point3 = PointF(0f, 0f)
            val point4 = PointF(0f, 0f)

            fun Canvas.drawPoint() {
//            canvas.drawCircle(point1.x, point1.y, 10f, Paint().apply {
//               this.color = Color.RED
//            })
//            canvas.drawCircle(point2.x, point2.y, 10f, Paint().apply {
//               this.color = Color.BLUE
//            })
//            canvas.drawCircle(point3.x, point3.y, 10f, Paint().apply {
//               this.color = Color.GREEN
//            })
//            canvas.drawCircle(point4.x, point4.y, 10f, Paint().apply {
//               this.color = Color.YELLOW
//            })
            }

            val MAX_ANGLE = 60f
            val angleRight = MAX_ANGLE * scaleToRight.toDouble()
            val angleLeft = MAX_ANGLE * scaleToLeft.toDouble()

            val angleTopRightX =
                (overCircleXCenter + overCircleRadius * cos(Math.toRadians(angleRight))).toFloat()
            val angleTopRightY =
                (overCircleYCenter - overCircleRadius * sin(Math.toRadians(angleRight))).toFloat()
            val angleTopLeftX =
                (overCircleXCenter - overCircleRadius * cos(Math.toRadians(angleLeft))).toFloat()
            val angleTopLeftY =
                (overCircleYCenter - overCircleRadius * sin(Math.toRadians(angleLeft))).toFloat()

            point1.set(
                WIDTH - backgroundMargin,
                overCircleYCenter - (circleRadius + circleRounded) * scaleToRight
            )
            point2.set(
                WIDTH - backgroundMargin,
                overCircleYCenter - (circleRadius + circleRounded) * scaleToRight
            )
            point3.set(WIDTH - backgroundMargin, overCircleYCenter - (circleRadius) * scaleToRight)
            point4.set(
                WIDTH - backgroundMargin - circleRounded * scaleToRight,
                overCircleYCenter - (circleRadius) * scaleToRight
            )

            this.lineTo(point1.x, point1.y)
            this.cubicTo(
                point2.x, point2.y,
                point3.x, point3.y,
                point4.x, point4.y,
            )

            point1.set(
                WIDTH - backgroundMargin - circleRounded * scaleToRight,
                overCircleYCenter - circleRadius * scaleToRight
            )
            point2.set(overCircleRect.right, overCircleYCenter - circleRadius * scaleToRight)
            point3.set(angleTopRightX, angleTopRightY)
            point4.set(angleTopRightX, angleTopRightY)
            canvas.drawPoint()

            this.cubicTo(
                point2.x, point2.y,
                point3.x, point3.y,
                point4.x, point4.y,
            )

            this.arcTo(
                overCircleRect,
                360f - angleRight.toFloat(),
                (angleLeft + angleRight).toFloat() - 180f
            )

            point1.set(angleTopLeftX.toFloat(), angleTopLeftY.toFloat())
            point2.set(angleTopLeftX.toFloat(), angleTopLeftY.toFloat())
            point3.set(overCircleRect.left, overCircleYCenter - (circleRadius) * scaleToLeft)
            point4.set(
                backgroundMargin + circleRounded * scaleToLeft,
                overCircleYCenter - circleRadius * scaleToLeft
            )

            this.lineTo(point1.x, point1.y)
            this.cubicTo(
                point2.x, point2.y,
                point3.x, point3.y,
                point4.x, point4.y,
            )

            point1.set(
                backgroundMargin + circleRounded * scaleToLeft,
                overCircleYCenter - circleRadius * scaleToLeft
            )
            point2.set(backgroundMargin, overCircleYCenter - circleRadius * scaleToLeft)
            point3.set(
                backgroundMargin,
                overCircleYCenter - (circleRadius + circleRounded) * scaleToLeft
            )
            point4.set(
                backgroundMargin,
                overCircleYCenter - (circleRadius + circleRounded) * scaleToLeft
            )
            this.cubicTo(
                point2.x, point2.y,
                point3.x, point3.y,
                point4.x, point4.y,
            )

            this.lineTo(
                backgroundMargin,
                overCircleYCenter + circleRadius * scaleToLeft
            )

            val angleBottomLeftX =
                overCircleXCenter - overCircleRadius * cos(Math.toRadians(angleLeft))
            val angleBottomLeftY =
                overCircleYCenter + overCircleRadius * sin(Math.toRadians(angleLeft))
            val angleBottomRightX =
                overCircleXCenter + overCircleRadius * cos(Math.toRadians(angleRight))
            val angleBottomRightY =
                overCircleYCenter + overCircleRadius * sin(Math.toRadians(angleRight))

            point1.set(
                backgroundMargin,
                overCircleYCenter + (circleRadius + circleRounded) * scaleToLeft
            )
            point2.set(
                backgroundMargin,
                overCircleYCenter + (circleRadius + circleRounded) * scaleToLeft
            )
            point3.set(backgroundMargin, overCircleYCenter + circleRadius * scaleToLeft)
            point4.set(
                backgroundMargin + circleRounded * scaleToLeft,
                overCircleYCenter + circleRadius * scaleToLeft
            )

            this.lineTo(point1.x, point1.y)
            this.cubicTo(
                point2.x, point2.y,
                point3.x, point3.y,
                point4.x, point4.y,
            )

            point1.set(
                backgroundMargin + circleRounded * scaleToLeft,
                overCircleYCenter + circleRadius * scaleToLeft
            )
            point2.set(overCircleRect.left, overCircleYCenter + circleRadius * scaleToLeft)
            point3.set(angleBottomLeftX.toFloat(), angleBottomLeftY.toFloat())
            point4.set(angleBottomLeftX.toFloat(), angleBottomLeftY.toFloat())

            this.cubicTo(
                point2.x, point2.y,
                point3.x, point3.y,
                point4.x, point4.y,
            )

            this.arcTo(
                overCircleRect,
                180f - angleLeft.toFloat(),
                (angleLeft + angleRight).toFloat() - 180f
            )

            point1.set(angleBottomRightX.toFloat(), angleBottomRightY.toFloat())
            point2.set(angleBottomRightX.toFloat(), angleBottomRightY.toFloat())
            point3.set(overCircleRect.right, overCircleYCenter + circleRadius * scaleToRight)
            point4.set(
                WIDTH - backgroundMargin - circleRounded * scaleToRight,
                overCircleYCenter + circleRadius * scaleToRight
            )

            this.lineTo(point1.x, point1.y)
            this.cubicTo(
                point2.x, point2.y,
                point3.x, point3.y,
                point4.x, point4.y,
            )

            point1.set(
                WIDTH - backgroundMargin - circleRounded * scaleToRight,
                overCircleYCenter + circleRadius * scaleToRight
            )
            point2.set(WIDTH - backgroundMargin, overCircleYCenter + circleRadius * scaleToRight)
            point3.set(
                WIDTH - backgroundMargin,
                overCircleYCenter + (circleRadius + circleRounded) * scaleToRight
            )
            point4.set(
                WIDTH - backgroundMargin,
                overCircleYCenter + (circleRadius + circleRounded) * scaleToRight
            )

            this.cubicTo(
                point2.x, point2.y,
                point3.x, point3.y,
                point4.x, point4.y,
            )

            this.lineTo(
                WIDTH - backgroundMargin,
                HEIGHT - backgroundMargin - backgroundRounded
            )

            this.arcTo(
                RectF(
                    WIDTH - backgroundMargin - backgroundRounded,
                    HEIGHT - backgroundMargin - backgroundRounded,
                    WIDTH - backgroundMargin,
                    HEIGHT - backgroundMargin
                ), 0f, 90f
            )

            this.lineTo(
                backgroundMargin + backgroundRounded,
                HEIGHT - backgroundMargin
            )

            this.arcTo(
                RectF(
                    backgroundMargin,
                    HEIGHT - backgroundMargin - backgroundRounded,
                    backgroundMargin + backgroundRounded,
                    HEIGHT - backgroundMargin
                ), 90f, 90f
            )

            this.lineTo(
                backgroundMargin,
                backgroundMargin + backgroundRounded
            )
        }

        canvas.drawPath(backGroundPath, backgroundShadowPaint)
        canvas.drawPath(backGroundPath, backgroundPaint)

        canvas.drawCircle(
            circleRect.centerX(),
            circleRect.centerY(),
            circleRect.width() / 2,
            backgroundShadowPaint
        )
        canvas.drawCircle(circleRect.centerX(),
            circleRect.centerY(),
            circleRect.width() / 2,
            circlePaint.apply {
                this.color = if (touch != null) Color.CYAN
                else Color.BLUE
            })

        val d = resources.getDrawable(R.drawable.icon_next, null)
        d.setBounds(
            circleRect.left.toInt(), circleRect.top.toInt(),
            circleRect.right.toInt(), circleRect.bottom.toInt()
        )
        d.draw(canvas)
    }

    private fun RectF.overSizeRect(spaceSize: Float): RectF {
        return RectF(
            this.left - spaceSize,
            this.top - spaceSize,
            this.right + spaceSize,
            this.bottom + spaceSize
        )
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if (event?.pointerCount == 1) {
            val used = when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val touchX = event.x
                    val touchY = event.y
                    touch = if (circleRect.isInSide(touchX, touchY)) {
                        moveToNearStepAnimate?.removeAllUpdateListeners()
                        PointF(
                            touchX, touchY
                        )
                    } else null
                    true
                }

                MotionEvent.ACTION_MOVE -> {
                    touch?.let { touch ->
                        val moveX = event.x - touch.x
                        circleRect.apply {
                            val size = this.width()
                            val newCircleLeft = this.left + moveX
                            val minCircleLeft = getMinCircleLeft()
                            val maxCircleLeft = getMaxCircleLeft()
                            val updateLeft = when {
                                newCircleLeft < minCircleLeft -> minCircleLeft
                                newCircleLeft > maxCircleLeft -> maxCircleLeft
                                else -> newCircleLeft
                            }
                            this.set(
                                updateLeft, this.top, updateLeft + size, this.top + size
                            )
                        }
                        this.touch = PointF(
                            event.x, event.y
                        )
                        invalidate()
                    }
                    true
                }

                MotionEvent.ACTION_UP -> {
                    touch = null
                    startMoveToNearStepAnimate()
                    true
                }

                else -> false
            }
            if (used) return true
        }
        return super.onTouchEvent(event)
    }

    private fun startMoveToNearStepAnimate() {
        val currentCenterX = circleRect.centerX()
        val circleRadius = circleSize / 2
        getStepNear(currentCenterX).let { stepIndex ->
            val isPlussing = stepIndex > circleRect.centerX()
            val rangeSize = abs(stepIndex - circleRect.centerX())
            if (rangeSize <= 0) return
            moveToNearStepAnimate = ValueAnimator.ofFloat(rangeSize).apply {
                this.duration = (rangeSize * 200 / backgroundRect.width()).toLong() + 100
                this.addUpdateListener { animation ->
                    (animation.animatedValue as? Float)?.let { animatedValue ->
                        val newLeft = if (isPlussing) currentCenterX + animatedValue - circleRadius
                        else currentCenterX - animatedValue - circleRadius
                        circleRect.set(
                            newLeft, circleRect.top, newLeft + circleSize, circleRect.bottom
                        )
                        invalidate()
                    }
                }
            }
            moveToNearStepAnimate?.start()
        }
    }

    private fun getMinCircleLeft(): Float {
        return context.dpToPx(CIRCLE_MARGIN_HORIZONTAL) + backgroundMargin
    }

    private fun getMaxCircleLeft(): Float {
        return backgroundRect.width() - context.dpToPx(CIRCLE_MARGIN_HORIZONTAL) - circleSize - backgroundMargin
    }

    private fun startDrag(): Float {
        return context.dpToPx(CIRCLE_MARGIN_HORIZONTAL) + backgroundMargin + circleSize / 2
    }

    private fun endDrag(): Float {
        return backgroundRect.width() - context.dpToPx(CIRCLE_MARGIN_HORIZONTAL) - circleSize / 2 - backgroundMargin
    }

    private fun stepDistance(): Float {
        return if (stepCount <= 1) 0f
        else abs(startDrag() - endDrag()) / (stepCount - 1)
    }

    private fun getStepNear(index: Float): Float {
        val start = context.dpToPx(CIRCLE_MARGIN_HORIZONTAL) + backgroundMargin + circleSize / 2
        val end =
            backgroundRect.width() - context.dpToPx(CIRCLE_MARGIN_HORIZONTAL) - circleSize / 2 - backgroundMargin

        if (stepCount == 1) {
            return (start + end) / 2
        } else {
            val stepSize = (end - start) / (stepCount - 1)
            var nearIndex = start
            var nearSize = abs(start - index)
            for (i in 1 until stepCount) {
                val stepIndex = (start + stepSize * i)
                val stepDistance = abs(stepIndex - index)
                if (nearSize > stepDistance) {
                    nearSize = stepDistance
                    nearIndex = stepIndex
                }
            }
            return nearIndex
        }
    }

    private fun RectF.isInSide(x: Float, y: Float): Boolean {
        return x >= this.left && x <= this.right && y >= this.top && y <= this.bottom
    }

    fun getButtonLocation(): Pair<Float, Float> {
        return circleRect.run {
            left to backgroundRect.bottom - bottom
        }
    }
}
