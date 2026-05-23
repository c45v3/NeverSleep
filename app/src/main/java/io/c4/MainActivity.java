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
        view.setText("NoSleepScope\n\nLSPosed module package: io.c4.ns\nXposed entry: io.c4.ns\nRecommended scope: com.luna.music\n\nEnable this module in LSPosed, select the target app scope, then force stop and reopen the target app. While the scoped app is in foreground, the module keeps its window screen-on flag enabled.");
        setContentView(view);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
