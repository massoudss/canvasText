package ir.logcat.canvastext.view.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.Typeface
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.view.View
import ir.logcat.canvastext.R
import ir.logcat.canvastext.utils.Utils

class AutoFitTextView(context: Context) : View(context) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val text = "لورم ایپسوم متن ساختگی"
    private var leftMargin = 0
    private var topMargin = 0
    private var w = 0
    private var h = 0
    private var padding = 0
    private val backgroundRect = Rect()
    private val backgroundPaint = Paint()

    init {

        textPaint.color = ContextCompat.getColor(context, R.color.white)
        textPaint.textAlign = Paint.Align.CENTER
        textPaint.typeface = Typeface.createFromAsset(context.assets, "fonts/iran_sans_light.ttf")

        topMargin = Utils.dpToPx(context, 32)
        leftMargin = topMargin
        w = Utils.dpToPx(context, 150)
        h = Utils.dpToPx(context, 50)
        padding = Utils.dpToPx(context, 16)

        backgroundRect.set(leftMargin, topMargin, leftMargin + w, topMargin + h)

        backgroundPaint.style = Paint.Style.FILL_AND_STROKE
        backgroundPaint.color = ContextCompat.getColor(context, R.color.colorAccent)

        val defaultTextSize = Utils.spToPx(context, 20)
        textPaint.textSize = defaultTextSize.toFloat()
        val desiredTextSize = defaultTextSize * backgroundRect.width() / (textPaint.measureText(text) + padding * 2)
        textPaint.textSize = desiredTextSize
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(backgroundRect, backgroundPaint)
        canvas.drawText(text, backgroundRect.centerX().toFloat(), backgroundRect.centerY() - (textPaint.ascent() + textPaint.descent()) / 2, textPaint)
    }
}
