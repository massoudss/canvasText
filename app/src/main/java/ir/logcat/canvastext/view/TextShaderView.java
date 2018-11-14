package ir.logcat.canvastext.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Shader;
import android.support.v4.content.ContextCompat;
import android.text.TextPaint;
import android.view.View;

import ir.logcat.canvastext.R;
import ir.logcat.canvastext.utils.Utils;

public class TextShaderView extends View {
    
    private TextPaint gradientTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint bitmapTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private TextPaint blurTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
    private String gradientText = "gradient shader text";
    private String bitmapText = "bitmap shader text";
    private String blurText = "blur mask text";
    private int leftMargin, topMargin;

    public TextShaderView(Context context) {
        super(context);
        init();
    }

    private void init() {

        gradientTextPaint.setTextSize(Utils.spToPx(getContext(), 30));

        bitmapTextPaint.setTextSize(Utils.spToPx(getContext(), 30));
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.pattern);
        bitmapTextPaint.setShader(new BitmapShader(bitmap,Shader.TileMode.MIRROR,Shader.TileMode.MIRROR));

        setLayerType(LAYER_TYPE_SOFTWARE,blurTextPaint);
        blurTextPaint.setColor(ContextCompat.getColor(getContext(),R.color.colorAccent));
        blurTextPaint.setTextSize(Utils.spToPx(getContext(), 30));
        float radius = Utils.dpToPx(getContext(),2);
        BlurMaskFilter blurMaskFilter = new BlurMaskFilter(radius,BlurMaskFilter.Blur.NORMAL);
        blurTextPaint.setMaskFilter(blurMaskFilter);

        leftMargin = topMargin = Utils.dpToPx(getContext(), 32);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText(gradientText, leftMargin, topMargin, gradientTextPaint);

        canvas.drawText(bitmapText, leftMargin, topMargin*2, bitmapTextPaint);

        canvas.drawText(blurText, leftMargin, topMargin*3, blurTextPaint);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        Shader shader = new LinearGradient(0f, 0f, 0f, w, new int[]{Color.RED, Color.BLUE, Color.GREEN}, null, Shader.TileMode.MIRROR);
        gradientTextPaint.setShader(shader);

        Matrix matrix = new Matrix();
        matrix.setRotate(90);
        shader.setLocalMatrix(matrix);

    }
}
