package ir.logcat.canvastext.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.View;

import ir.logcat.canvastext.R;
import ir.logcat.canvastext.utils.Utils;

public class SimpleTextView extends View {

    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private String text = "This is a simple text on canvas";
    private int leftMargin, topMargin;

    public SimpleTextView(Context context) {
        super(context);
        init();
    }

    private void init() {

        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        textPaint.setTextSize(Utils.spToPx(getContext(), 20));

        leftMargin = topMargin = Utils.dpToPx(getContext(), 32);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(text, leftMargin, topMargin, textPaint);
    }
}
