package ir.logcat.canvastext.view.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.DynamicLayout
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import android.view.View
import ir.logcat.canvastext.R
import ir.logcat.canvastext.utils.Utils

class MultilineTextView(context: Context) : View(context) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Egestas purus viverra accumsan in nisl nisi.\nلورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است. چاپگرها و متون بلکه روزنامه و مجله در ستون و سطرآنچنان که لازم است."
    private var leftMargin = 0
    private var topMargin = 0
    private var rightMargin = 0
    private var screenWidth = 0
    private var staticLayout: StaticLayout? = null
    private var dynamicLayout: DynamicLayout? = null

    init  {
        textPaint.color = ContextCompat.getColor(context, R.color.black)
        textPaint.textSize = Utils.spToPx(context, 20).toFloat()
        textPaint.typeface = Typeface.createFromAsset(context.assets, "fonts/iran_sans_light.ttf")

        rightMargin = Utils.dpToPx(context, 32)
        topMargin = rightMargin
        leftMargin = topMargin
        screenWidth = context.resources.displayMetrics.widthPixels

        val maxWidth = screenWidth - leftMargin - rightMargin
        val spacingAdd = 0
        val spacingMult = 1

        staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val sb = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, maxWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(spacingAdd.toFloat(), spacingMult.toFloat())
                    .setIncludePad(false)
            sb.build()
        } else {
            StaticLayout(text, textPaint, maxWidth, Layout.Alignment.ALIGN_NORMAL, spacingMult.toFloat(), spacingAdd.toFloat(), false)
        }
        //if your text will change use DynamicLayout instead of StaticLayout
        dynamicLayout = if (Build.VERSION.SDK_INT >= 28) {
            val sb = DynamicLayout.Builder.obtain(text, textPaint, maxWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(spacingAdd.toFloat(), spacingMult.toFloat())
                    .setIncludePad(false)
            sb.build()
        } else {
            DynamicLayout(text, text, textPaint, maxWidth, Layout.Alignment.ALIGN_NORMAL, spacingMult.toFloat(), spacingAdd.toFloat(), false)
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(leftMargin.toFloat(), topMargin.toFloat())
        staticLayout!!.draw(canvas)
        canvas.restore()

        canvas.drawLine(0f,
                (Utils.dpToPx(context, 48) + staticLayout!!.height).toFloat(),
                screenWidth.toFloat(), (Utils.dpToPx(context, 48) + staticLayout!!.height).toFloat(), textPaint)

        canvas.save()
        canvas.translate(leftMargin.toFloat(), (topMargin * 2 + staticLayout!!.height).toFloat())
        dynamicLayout!!.draw(canvas)
        canvas.restore()
    }
}