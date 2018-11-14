package ir.logcat.canvastext.view.java;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.View;

import ir.logcat.canvastext.R;
import ir.logcat.canvastext.utils.Utils;

public class MultilineTextView extends View {

    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private String text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Egestas purus viverra accumsan in nisl nisi.\nلورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است. چاپگرها و متون بلکه روزنامه و مجله در ستون و سطرآنچنان که لازم است.";
    private int leftMargin, topMargin, rightMargin, screenWidth;
    private StaticLayout staticLayout;
    private DynamicLayout dynamicLayout;

    public MultilineTextView(Context context) {
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
        int spacingAdd = 0;
        int spacingMult = 1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder sb = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, maxWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(spacingAdd, spacingMult)
                    .setIncludePad(false);
            staticLayout = sb.build();
        } else {
            staticLayout = new StaticLayout(text,textPaint,maxWidth,Layout.Alignment.ALIGN_NORMAL,spacingMult,spacingAdd,false);
        }
        //if your text will change use DynamicLayout instead of StaticLayout
        if (Build.VERSION.SDK_INT >= 28) {
            DynamicLayout.Builder sb = DynamicLayout.Builder.obtain(text,textPaint, maxWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(spacingAdd, spacingMult)
                    .setIncludePad(false);
            dynamicLayout = sb.build();
        } else {
            dynamicLayout = new DynamicLayout(text,text,textPaint,maxWidth,Layout.Alignment.ALIGN_NORMAL,spacingMult,spacingAdd,false);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(leftMargin, topMargin);
        staticLayout.draw(canvas);
        canvas.restore();

        canvas.drawLine(0,
                Utils.dpToPx(getContext(), 48) + staticLayout.getHeight(),
                screenWidth, Utils.dpToPx(getContext(), 48) + staticLayout.getHeight(), textPaint);

        canvas.save();
        canvas.translate(leftMargin, topMargin * 2 + staticLayout.getHeight());
        dynamicLayout.draw(canvas);
        canvas.restore();
    }
}
