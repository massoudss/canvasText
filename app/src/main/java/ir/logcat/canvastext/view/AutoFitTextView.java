package ir.logcat.canvastext.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.View;

import ir.logcat.canvastext.R;
import ir.logcat.canvastext.utils.Utils;

public class AutoFitTextView extends View {

    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private String text = "لورم ایپسوم متن ساختگی";
    private int leftMargin, topMargin, width, height, padding;
    private Rect backgroundRect = new Rect();
    private Paint backgroundPaint = new Paint();

    public AutoFitTextView(Context context) {
        super(context);
        init();
    }

    private void init() {

        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.white));
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_sans_light.ttf"));

        leftMargin = topMargin = Utils.dpToPx(getContext(), 32);
        width = Utils.dpToPx(getContext(), 150);
        height = Utils.dpToPx(getContext(), 50);
        padding = Utils.dpToPx(getContext(), 16);


        backgroundRect.set(leftMargin, topMargin, leftMargin + width, topMargin + height);

        backgroundPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        backgroundPaint.setColor(ContextCompat.getColor(getContext(), R.color.colorAccent));

        int defaultTextSize = Utils.spToPx(getContext(), 20);
        textPaint.setTextSize(defaultTextSize);
        float desiredTextSize = defaultTextSize * backgroundRect.width() / (textPaint.measureText(text) + padding * 2);
        textPaint.setTextSize(desiredTextSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRect(backgroundRect, backgroundPaint);
        canvas.drawText(text, backgroundRect.centerX(), backgroundRect.centerY() - (textPaint.ascent() + textPaint.descent()) / 2, textPaint);
    }
}
