package ir.logcat.canvastext.view.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.text.TextUtils
import android.view.View
import ir.logcat.canvastext.R
import ir.logcat.canvastext.utils.StaticLayoutWithMaxLines
import ir.logcat.canvastext.utils.Utils


class EllipsizedTextView(context: Context) : View(context) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val text = "لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است."
    private var trimmedText: String? = null
    private var leftMargin = 0
    private var topMargin = 0
    private var rightMargin = 0
    private var screenWidth = 0
    private var staticLayout: StaticLayout? = null

    init {

        textPaint.color = ContextCompat.getColor(context, R.color.black)
        textPaint.textSize = Utils.spToPx(context, 20).toFloat()
        textPaint.typeface = Typeface.createFromAsset(context.assets, "fonts/iran_sans_light.ttf")

        rightMargin = Utils.dpToPx(context, 32)
        topMargin = rightMargin
        leftMargin = topMargin
        screenWidth = context.resources.displayMetrics.widthPixels
        val maxWidth = screenWidth - leftMargin - rightMargin

        trimmedText = TextUtils.ellipsize(text, textPaint, maxWidth.toFloat(), TextUtils.TruncateAt.END).toString()

        val spacingAdd = 0
        val spacingMult = 1

        staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val sb = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, maxWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(spacingAdd.toFloat(), spacingMult.toFloat())
                    .setMaxLines(2)
                    .setEllipsize(TextUtils.TruncateAt.END)
                    .setIncludePad(false)
            sb.build()
        } else
            StaticLayoutWithMaxLines.create(text, 0, text.length,
                    textPaint, maxWidth,
                    Layout.Alignment.ALIGN_NORMAL,
                    spacingMult.toFloat(), spacingAdd.toFloat(), false, TextUtils.TruncateAt.END, maxWidth, 2)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawText(trimmedText!!, leftMargin.toFloat(), topMargin.toFloat(), textPaint)

        canvas.drawLine(0f, (canvas.height / 2).toFloat(), screenWidth.toFloat(), (canvas.height / 2).toFloat(), textPaint)

        canvas.save()
        canvas.translate(leftMargin.toFloat(), (topMargin + canvas.height / 2).toFloat())
        staticLayout!!.draw(canvas)
        canvas.restore()
    }
}
