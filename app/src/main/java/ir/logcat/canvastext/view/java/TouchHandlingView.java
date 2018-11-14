package ir.logcat.canvastext.view.java;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Toast;

import ir.logcat.canvastext.R;
import ir.logcat.canvastext.utils.Utils;

public class TouchHandlingView extends View {

    private static final long DOUBLE_CLICK_DELAY = 200;
    private static final long LONG_CLICK_DELAY = 500;
    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private String text = "لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از طراحان گرافیک است. چاپگرها و متون بلکه روزنامه و مجله در ستون و سطرآنچنان که لازم است.";
    private int leftMargin, topMargin, rightMargin, screenWidth;
    private StaticLayout staticLayout;
    private long lastClickTime = 0;
    private boolean longClickStarted = false, wasLongClick = false;
    private ScaleGestureDetector scaleGestureDetector;
    private float scaleFactor = 1.0f;
    private float textSize;
    private int maxWidth, spacingAdd, spacingMult;

    public TouchHandlingView(Context context) {
        super(context);
        init();
    }

    private void init() {

        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        textPaint.setTextSize(Utils.spToPx(getContext(), 20));
        textPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_sans_light.ttf"));

        leftMargin = topMargin = rightMargin = Utils.dpToPx(getContext(), 32);
        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;

        textSize = Utils.spToPx(getContext(), 20);
        maxWidth = screenWidth - leftMargin - rightMargin;
        spacingAdd = 0;
        spacingMult = 1;

        createText();

        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(leftMargin, topMargin);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    private void createText() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder sb = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, maxWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(spacingAdd, spacingMult)
                    .setIncludePad(false);
            staticLayout = sb.build();
        } else
            staticLayout = new StaticLayout(text, textPaint, maxWidth, Layout.Alignment.ALIGN_NORMAL, spacingMult, spacingAdd, false);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        scaleGestureDetector.onTouchEvent(event);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                Toast.makeText(getContext(), "touch", Toast.LENGTH_SHORT).show();

                longClickStarted = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (longClickStarted) {
                            Toast.makeText(getContext(), "long press", Toast.LENGTH_SHORT).show();
                            wasLongClick = true;
                        }
                    }
                }, LONG_CLICK_DELAY);

                return true;

            case MotionEvent.ACTION_UP:

                longClickStarted = false;

                if (wasLongClick) {
                    wasLongClick = false;
                    return true;
                }

                if (System.currentTimeMillis() - lastClickTime < DOUBLE_CLICK_DELAY) {
                    Toast.makeText(getContext(), "double top", Toast.LENGTH_SHORT).show();
                    lastClickTime = 0;
                    return true;
                }
                lastClickTime = System.currentTimeMillis();

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (lastClickTime != 0 && System.currentTimeMillis() - lastClickTime >= DOUBLE_CLICK_DELAY) {
                            Toast.makeText(getContext(), "single tap", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, DOUBLE_CLICK_DELAY);

                return true;
        }
        return false;
    }



    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {

        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {

            scaleFactor *= scaleGestureDetector.getScaleFactor();

            scaleFactor = Math.max(0.1f, Math.min(scaleFactor, 10.0f));

            textPaint.setTextSize(textSize * scaleFactor);

            createText();

            invalidate();

            return true;

        }
    }
}
