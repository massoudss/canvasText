package ir.logcat.canvastext.view.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.os.Handler
import android.support.v4.content.ContextCompat
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import android.widget.Toast
import ir.logcat.canvastext.R
import ir.logcat.canvastext.utils.Utils

class TouchHandlingView(context: Context) : View(context) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val text = "لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است. چاپگرها و متون بلکه روزنامه و مجله در ستون و سطرآنچنان که لازم است."
    private var leftMargin = 0
    private var topMargin = 0
    private var rightMargin = 0
    private var screenWidth = 0
    private var staticLayout: StaticLayout? = null
    private var lastClickTime = 0
    private var longClickStarted = false
    private var wasLongClick = false
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var scaleFactor = 1.0f
    private var textSize = 0f
    private var maxWidth = 0
    private var spacingAdd = 0
    private var spacingMult = 0

    companion object {
        private val DOUBLE_CLICK_DELAY: Long = 200
        private val LONG_CLICK_DELAY: Long = 500
    }

    init {

        textPaint.color = ContextCompat.getColor(context, R.color.black)
        textPaint.textSize = Utils.spToPx(context, 20).toFloat()
        textPaint.typeface = Typeface.createFromAsset(context.assets, "fonts/iran_sans_light.ttf")

        rightMargin = Utils.dpToPx(context, 32)
        topMargin = rightMargin
        leftMargin = topMargin
        screenWidth = context.resources.displayMetrics.widthPixels

        textSize = Utils.spToPx(context, 20).toFloat()
        maxWidth = screenWidth - leftMargin - rightMargin
        spacingAdd = 0
        spacingMult = 1

        createText()

        scaleGestureDetector = ScaleGestureDetector(context, ScaleListener())
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.translate(leftMargin.toFloat(), topMargin.toFloat())
        staticLayout!!.draw(canvas)
        canvas.restore()
    }

    private fun createText() {

        staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val sb = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, maxWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(spacingAdd.toFloat(), spacingMult.toFloat())
                    .setIncludePad(false)
            sb.build()
        } else
            StaticLayout(text, textPaint, maxWidth, Layout.Alignment.ALIGN_NORMAL, spacingMult.toFloat(), spacingAdd.toFloat(), false)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        scaleGestureDetector!!.onTouchEvent(event)

        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

                Toast.makeText(context, "touch", Toast.LENGTH_SHORT).show()

                longClickStarted = true
                Handler().postDelayed({
                    if (longClickStarted) {
                        Toast.makeText(context, "long press", Toast.LENGTH_SHORT).show()
                        wasLongClick = true
                    }
                }, LONG_CLICK_DELAY)

                return true
            }

            MotionEvent.ACTION_UP -> {

                longClickStarted = false

                if (wasLongClick) {
                    wasLongClick = false
                    return true
                }

                if (System.currentTimeMillis() - lastClickTime < DOUBLE_CLICK_DELAY) {
                    Toast.makeText(context, "double top", Toast.LENGTH_SHORT).show()
                    lastClickTime = 0
                    return true
                }
                lastClickTime = System.currentTimeMillis().toInt()

                Handler().postDelayed({
                    if (lastClickTime != 0 && System.currentTimeMillis() - lastClickTime >= DOUBLE_CLICK_DELAY) {
                        Toast.makeText(context, "single tap", Toast.LENGTH_SHORT).show()
                    }
                }, DOUBLE_CLICK_DELAY)

                return true
            }
        }
        return false
    }


    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {

        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {

            scaleFactor *= scaleGestureDetector.scaleFactor

            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f))

            textPaint.textSize = textSize * scaleFactor

            createText()

            invalidate()

            return true

        }
    }
}
