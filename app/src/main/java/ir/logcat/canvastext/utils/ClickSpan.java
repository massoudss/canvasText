package ir.logcat.canvastext.utils;

import android.text.TextPaint;
import android.text.style.ClickableSpan;
import android.view.View;

public class ClickSpan extends ClickableSpan {

    private int color;
    private String text;

    public ClickSpan(int color,String text) {
        this.color = color;
        this.text = text;
    }

    @Override
    public void updateDrawState(TextPaint ds) {
        super.updateDrawState(ds);
        ds.setColor(color);
        ds.setUnderlineText(true);
    }

    @Override
    public void onClick(View widget) {
        // do something when text clicked
        // this method not working on canvas :D
    }

    public String getText() {
        return text;
    }
}
