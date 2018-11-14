package ir.logcat.canvastext.view.kotlin

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.os.Build
import android.support.v4.content.ContextCompat
import android.text.*
import android.text.style.*
import android.view.MotionEvent
import android.view.View
import android.widget.Toast
import ir.logcat.canvastext.R
import ir.logcat.canvastext.utils.ClickSpan
import ir.logcat.canvastext.utils.TextPath
import ir.logcat.canvastext.utils.Utils
import java.util.regex.Matcher
import java.util.regex.Pattern


class SpannedTextView(context: Context) : View(context) {

    private val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG)
    private val text = SpannableStringBuilder("Lorem ipsum LOL sit amet, consectetur #adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Egestas purus viverra accumsan in nisl nisi.\nلورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از #طراحان گرافیک است. چاپگرها و متون بلکه #روزنامه و مجله در ستون و سطرآنچنان که لازم است.")
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

        tintMe("Lorem")
        tintMe("گرافیک")
        highlightMe("consectetur")
        highlightMe("متن ساختگی")
        replaceMe("LOL")
        subscriptMe("بلکه")
        superscriptMe("tempor")
        scaleXMe("accumsan")
        highlightHashtags()

        val maxWidth = screenWidth - leftMargin - rightMargin
        val spacingAdd = 0
        val spacingMult = 1

        staticLayout = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val sb = StaticLayout.Builder.obtain(text, 0, text.length, textPaint, maxWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(spacingAdd.toFloat(), spacingMult.toFloat())
                    .setIncludePad(false)
            sb.build()
        } else
            StaticLayout(text, textPaint, maxWidth, Layout.Alignment.ALIGN_NORMAL, spacingMult.toFloat(), spacingAdd.toFloat(), false)
    }

    private fun highlightHashtags() {
        val matcher = getMatches("#([A-Za-z0-9_-۰-۹ا-ی]+)")
        while (matcher.find()) {
            text.setSpan(ClickSpan(Color.BLUE, matcher.group()), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun highlightMe(textToHighlight: String) {

        val matcher = getMatches(textToHighlight)
        while (matcher.find()) {
            this.text.setSpan(BackgroundColorSpan(Color.YELLOW), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun tintMe(textToTint: String) {
        val matcher = getMatches(textToTint)
        while (matcher.find()) {
            this.text.setSpan(ForegroundColorSpan(Color.RED), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            this.text.setSpan(RelativeSizeSpan(2f), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

    }

    private fun replaceMe(textToImage: String) {
        val drawable = ContextCompat.getDrawable(context, R.drawable.emoji)
        drawable!!.setBounds(0, 0, Utils.dpToPx(context, 30), Utils.dpToPx(context, 30))

        val matcher = getMatches(textToImage)
        while (matcher.find()) {
            text.setSpan(ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }

    }

    private fun subscriptMe(textToSub: String) {
        val matcher = getMatches(textToSub)
        while (matcher.find()) {
            text.setSpan(SubscriptSpan(), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun superscriptMe(textToSuper: String) {
        val matcher = getMatches(textToSuper)
        while (matcher.find()) {
            text.setSpan(SuperscriptSpan(), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun scaleXMe(textToScale: String) {
        val matcher = getMatches(textToScale)
        while (matcher.find()) {
            text.setSpan(ScaleXSpan(2f), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    private fun getMatches(filter: String): Matcher {
        val tx = text.toString()
        return Pattern.compile(filter).matcher(tx)
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.save()
        canvas.translate(leftMargin.toFloat(), topMargin.toFloat())
        staticLayout!!.draw(canvas)
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.action) {
            MotionEvent.ACTION_DOWN ->

                return true
            MotionEvent.ACTION_UP ->

                return isHashTagClicked(event)
        }

        return false
    }

    private fun isHashTagClicked(event: MotionEvent): Boolean {

        var x = event.x.toInt()
        var y = event.y.toInt()
        val captionX = leftMargin
        val captionY = topMargin

        try {
            x -= captionX
            y -= captionY
            val line = staticLayout!!.getLineForVertical(y)
            val off = staticLayout!!.getOffsetForHorizontal(line, x.toFloat())

            val left = staticLayout!!.getLineLeft(line)

            if (left <= x && left + staticLayout!!.getLineWidth(line) >= x) {

                val buffer = SpannableString(text)
                val link = buffer.getSpans(off, off, ClickSpan::class.java)
                if (link == null || link.isEmpty())
                    return false

                val pressedLink = link[0]

                Toast.makeText(context, pressedLink.text, Toast.LENGTH_SHORT).show()

                try {
                    val path = TextPath()
                    val start = buffer.getSpanStart(pressedLink)
                    path.setCurrentLayout(staticLayout, start, 0f)
                    staticLayout!!.getSelectionPath(start, buffer.getSpanEnd(pressedLink), path)
                } catch (e: Exception) {
                    e.printStackTrace()
                }

                return true
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return false
    }
}
