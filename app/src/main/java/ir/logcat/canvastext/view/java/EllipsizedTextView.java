package ir.logcat.canvastext.view.java;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.view.View;

import ir.logcat.canvastext.R;
import ir.logcat.canvastext.utils.StaticLayoutWithMaxLines;
import ir.logcat.canvastext.utils.Utils;

public class EllipsizedTextView extends View {

    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private String text = "لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است.";
    private String trimmedText;
    private int leftMargin, topMargin, rightMargin, screenWidth;
    private StaticLayout staticLayout;

    public EllipsizedTextView(Context context) {
        super(context);
        init();
    }

    private void init() {

        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        textPaint.setTextSize(Utils.spToPx(getContext(), 20));
        textPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_sans_light.ttf"));

        leftMargin = topMargin = rightMargin = Utils.dpToPx(getContext(), 32);
        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;
        int maxWidth = screenWidth - leftMargin - rightMargin;

        trimmedText = TextUtils.ellipsize(text, textPaint, maxWidth, TextUtils.TruncateAt.END).toString();

        int spacingAdd = 0;
        int spacingMult = 1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder sb = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, maxWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(spacingAdd, spacingMult)
                    .setMaxLines(2)
                    .setEllipsize(TextUtils.TruncateAt.END)
                    .setIncludePad(false);
            staticLayout = sb.build();
        } else
            staticLayout = StaticLayoutWithMaxLines.create(text, 0, text.length(),
                    textPaint, maxWidth,
                    Layout.Alignment.ALIGN_NORMAL,
                    spacingMult, spacingAdd, false, TextUtils.TruncateAt.END, maxWidth, 2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(trimmedText, leftMargin, topMargin, textPaint);


        canvas.drawLine(0, canvas.getHeight() / 2, screenWidth, canvas.getHeight() / 2, textPaint);

        canvas.save();
        canvas.translate(leftMargin, topMargin + canvas.getHeight() / 2);
        staticLayout.draw(canvas);
        canvas.restore();
    }
}
