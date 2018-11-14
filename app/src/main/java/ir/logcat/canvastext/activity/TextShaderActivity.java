package ir.logcat.canvastext.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ir.logcat.canvastext.view.TextShaderView;

public class TextShaderActivity extends AppCompatActivity {

    TextShaderView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new TextShaderView(this);
        setContentView(view);
    }


}
