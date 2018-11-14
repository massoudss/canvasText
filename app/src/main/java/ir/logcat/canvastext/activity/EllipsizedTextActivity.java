package ir.logcat.canvastext.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ir.logcat.canvastext.view.EllipsizedTextView;

public class EllipsizedTextActivity extends AppCompatActivity {

    EllipsizedTextView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new EllipsizedTextView(this);
        setContentView(view);
        setTitle(EllipsizedTextActivity.class.getSimpleName());
    }
}
