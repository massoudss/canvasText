package ir.logcat.canvastext.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.ScaleXSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ir.logcat.canvastext.R;
import ir.logcat.canvastext.utils.ClickSpan;
import ir.logcat.canvastext.utils.TextPath;
import ir.logcat.canvastext.utils.Utils;

public class SpannedTextView extends View {

    private TextPaint textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private SpannableStringBuilder text = new SpannableStringBuilder("Lorem ipsum LOL sit amet, consectetur #adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Egestas purus viverra accumsan in nisl nisi.\nلورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ و با استفاده از #طراحان گرافیک است. چاپگرها و متون بلکه #روزنامه و مجله در ستون و سطرآنچنان که لازم است.");
    private int leftMargin, topMargin, rightMargin, screenWidth;
    private StaticLayout staticLayout;

    public SpannedTextView(Context context) {
        super(context);
        init();
    }

    private void init() {

        textPaint.setColor(ContextCompat.getColor(getContext(), R.color.black));
        textPaint.setTextSize(Utils.spToPx(getContext(), 20));
        textPaint.setTypeface(Typeface.createFromAsset(getContext().getAssets(), "fonts/iran_sans_light.ttf"));

        leftMargin = topMargin = rightMargin = Utils.dpToPx(getContext(), 32);
        screenWidth = getContext().getResources().getDisplayMetrics().widthPixels;

        tintMe("Lorem");
        tintMe("گرافیک");
        hightlightMe("consectetur");
        hightlightMe("متن ساختگی");
        replaceMe("LOL");
        subscriptMe("بلکه");
        superscriptMe("tempor");
        scaleXMe("accumsan");
        highLightHashtags();

        int maxWidth = screenWidth - leftMargin - rightMargin;
        int spacingAdd = 0;
        int spacingMult = 1;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            StaticLayout.Builder sb = StaticLayout.Builder.obtain(text, 0, text.length(), textPaint, maxWidth)
                    .setAlignment(Layout.Alignment.ALIGN_NORMAL)
                    .setLineSpacing(spacingAdd, spacingMult)
                    .setIncludePad(false);
            staticLayout = sb.build();
        } else
            staticLayout = new StaticLayout(text, textPaint, maxWidth, Layout.Alignment.ALIGN_NORMAL, spacingMult, spacingAdd, false);
    }

    private void highLightHashtags() {
        Matcher matcher = getMatches("#([A-Za-z0-9_-۰-۹ا-ی]+)");
        while (matcher.find()) {
            text.setSpan(new ClickSpan(Color.BLUE,matcher.group()), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void hightlightMe(String textToHighlight) {

        Matcher matcher = getMatches(textToHighlight);
        while (matcher.find()) {
            this.text.setSpan(new BackgroundColorSpan(Color.YELLOW), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    public void tintMe(String textToTint) {
        Matcher matcher = getMatches(textToTint);
        while (matcher.find()) {
            this.text.setSpan(new ForegroundColorSpan(Color.RED), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            this.text.setSpan(new RelativeSizeSpan(2f), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    private void replaceMe(String textToImage) {
        Drawable drawable = ContextCompat.getDrawable(getContext(), R.drawable.emoji);
        drawable.setBounds(0, 0, Utils.dpToPx(getContext(), 30), Utils.dpToPx(getContext(), 30));

        Matcher matcher = getMatches(textToImage);
        while (matcher.find()) {
            text.setSpan(new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

    }

    private void subscriptMe(String textToSub) {
        Matcher matcher = getMatches(textToSub);
        while (matcher.find()) {
            text.setSpan(new SubscriptSpan(), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void superscriptMe(String textToSuper) {
        Matcher matcher = getMatches(textToSuper);
        while (matcher.find()) {
            text.setSpan(new SuperscriptSpan(), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private void scaleXMe(String textToScale) {
        Matcher matcher = getMatches(textToScale);
        while (matcher.find()) {
            text.setSpan(new ScaleXSpan(2F), matcher.start(), matcher.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private Matcher getMatches(String filter) {
        String tx = text.toString();
        return Pattern.compile(filter).matcher(tx);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.save();
        canvas.translate(leftMargin, topMargin);
        staticLayout.draw(canvas);
        canvas.restore();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                return true;
            case MotionEvent.ACTION_UP:

                return isHashTagClicked(event);
        }

        return false;
    }

    private boolean isHashTagClicked(MotionEvent event) {

        int x = (int) event.getX();
        int y = (int) event.getY();
        int captionX = leftMargin;
        int captionY = topMargin;

        try {
            x -= captionX;
            y -= captionY;
            final int line = staticLayout.getLineForVertical(y);
            final int off = staticLayout.getOffsetForHorizontal(line, x);

            final float left = staticLayout.getLineLeft(line);

            if (left <= x && left + staticLayout.getLineWidth(line) >= x) {

                Spannable buffer = new SpannableString(text);
                ClickSpan[] link = buffer.getSpans(off, off, ClickSpan.class);
                if (link == null || link.length == 0)
                    return false;

                ClickSpan pressedLink = link[0];


                Toast.makeText(getContext(), pressedLink.getText(), Toast.LENGTH_SHORT).show();

                try {
                    TextPath path = new TextPath();
                    int start = buffer.getSpanStart(pressedLink);
                    path.setCurrentLayout(staticLayout, start, 0);
                    staticLayout.getSelectionPath(start, buffer.getSpanEnd(pressedLink), path);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                invalidate();
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
