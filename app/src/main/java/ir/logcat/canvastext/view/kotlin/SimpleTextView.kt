package ir.logcat.canvastext.view.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.view.View
import ir.logcat.canvastext.R
import ir.logcat.canvastext.utils.Utils

class SimpleTextView(context: Context) : View(context){

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val text = "This is a simple text on canvas"
    private var leftMargin = 0
    private var topMargin = 0

    init  {
        textPaint.color = ContextCompat.getColor(getContext(), R.color.black)
        textPaint.textSize = Utils.spToPx(getContext(), 20).toFloat()
        topMargin = Utils.dpToPx(getContext(), 32)
        leftMargin = topMargin
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawText(text, leftMargin.toFloat(), topMargin.toFloat(), textPaint)
    }
}