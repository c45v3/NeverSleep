package io.c4;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public final class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TextView view = new TextView(this);
        int pad = dp(20);
        view.setPadding(pad, pad, pad, pad);
        view.setTextSize(16f);
        view.setText("NeverSleep\n\nPackage: io.c4.ns\nEntry: io.c4.ns\nRecommended scope: com.luna.music\n\nEnable in LSPosed, select scope, then force stop and reopen the target app.");
        setContentView(view);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
