package ir.logcat.canvastext.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ir.logcat.canvastext.view.MultilineTextView;

public class MultilineTextActivity extends AppCompatActivity {

    MultilineTextView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new MultilineTextView(this);
        setContentView(view);
        setTitle(MultilineTextActivity.class.getSimpleName());
    }
}
