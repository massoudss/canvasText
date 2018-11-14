package ir.logcat.canvastext.view.kotlin

import android.content.Context
import android.graphics.*
import android.support.v4.content.ContextCompat
import android.text.TextPaint
import android.view.View
import ir.logcat.canvastext.R
import ir.logcat.canvastext.utils.Utils

class TextShaderView(context: Context) : View(context) {

    private val gradientTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val bitmapTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val blurTextPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val gradientText = "gradient shader text"
    private val bitmapText = "bitmap shader text"
    private val blurText = "blur mask text"
    private var leftMargin = 0
    private var topMargin = 0

    init {

        gradientTextPaint.textSize = Utils.spToPx(context, 30).toFloat()

        bitmapTextPaint.textSize = Utils.spToPx(context, 30).toFloat()
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.pattern)
        bitmapTextPaint.shader = BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)

        setLayerType(View.LAYER_TYPE_SOFTWARE, blurTextPaint)
        blurTextPaint.color = ContextCompat.getColor(context, R.color.colorAccent)
        blurTextPaint.textSize = Utils.spToPx(context, 30).toFloat()
        val radius = Utils.dpToPx(context, 2).toFloat()
        val blurMaskFilter = BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL)
        blurTextPaint.maskFilter = blurMaskFilter

        topMargin = Utils.dpToPx(context, 32)
        leftMargin = topMargin
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawText(gradientText, leftMargin.toFloat(), topMargin.toFloat(), gradientTextPaint)

        canvas.drawText(bitmapText, leftMargin.toFloat(), (topMargin * 2).toFloat(), bitmapTextPaint)

        canvas.drawText(blurText, leftMargin.toFloat(), (topMargin * 3).toFloat(), blurTextPaint)
    }


    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)

        val shader = LinearGradient(0f, 0f, 0f, w.toFloat(), intArrayOf(Color.RED, Color.BLUE, Color.GREEN), null, Shader.TileMode.MIRROR)
        gradientTextPaint.shader = shader

        val matrix = Matrix()
        matrix.setRotate(90f)
        shader.setLocalMatrix(matrix)

    }
}
