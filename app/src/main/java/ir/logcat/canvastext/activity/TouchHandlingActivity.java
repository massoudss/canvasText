package ir.logcat.canvastext.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import ir.logcat.canvastext.view.TouchHandlingView;

public class TouchHandlingActivity extends AppCompatActivity {

    TouchHandlingView view;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        view = new TouchHandlingView(this);
        setContentView(view);
    }
}
