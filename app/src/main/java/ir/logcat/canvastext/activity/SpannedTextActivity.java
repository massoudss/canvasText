package ir.logcat.canvastext.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ir.logcat.canvastext.view.SpannedTextView;

public class SpannedTextActivity extends AppCompatActivity {

    SpannedTextView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new SpannedTextView(this);
        setContentView(view);
        setTitle(SpannedTextActivity.class.getSimpleName());
    }
}
