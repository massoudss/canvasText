package ir.logcat.canvastext.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ir.logcat.canvastext.view.java.SimpleTextView;

public class SimpleTextActivity extends AppCompatActivity {

    SimpleTextView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new SimpleTextView(this);
        setContentView(view);
        setTitle(SimpleTextActivity.class.getSimpleName());
    }
}
