package ir.logcat.canvastext.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ir.logcat.canvastext.view.java.AutoFitTextView;

public class AutoFitTextActivity extends AppCompatActivity {

    AutoFitTextView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new AutoFitTextView(this);
        setContentView(view);
        setTitle(AutoFitTextActivity.class.getSimpleName());
    }
}
