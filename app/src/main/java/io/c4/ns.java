package io.c4;

import android.app.Activity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public final class ns implements IXposedHookLoadPackage {
    private static final String TAG = "NeverSleep";
    private static final int KEEP_SCREEN_ON = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) {
        if ("io.c4.ns".equals(lpparam.packageName)) {
            return;
        }

        hookActivityLifecycle();
        hookWindowClearFlags();
        hookViewSetKeepScreenOn();

        XposedBridge.log(TAG + ": enabled for " + lpparam.packageName);
    }

    private static void hookActivityLifecycle() {
        XposedHelpers.findAndHookMethod(Activity.class, "onCreate", android.os.Bundle.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                applyKeepScreenOn((Activity) param.thisObject);
            }
        });

        XposedHelpers.findAndHookMethod(Activity.class, "onResume", new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                applyKeepScreenOn((Activity) param.thisObject);
            }
        });

        XposedHelpers.findAndHookMethod(Activity.class, "onWindowFocusChanged", boolean.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) {
                if ((Boolean) param.args[0]) {
                    applyKeepScreenOn((Activity) param.thisObject);
                }
            }
        });
    }

    private static void hookWindowClearFlags() {
        XposedHelpers.findAndHookMethod(Window.class, "clearFlags", int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                int flags = (Integer) param.args[0];
                if ((flags & KEEP_SCREEN_ON) != 0) {
                    param.args[0] = flags & ~KEEP_SCREEN_ON;
                }
            }
        });
    }

    private static void hookViewSetKeepScreenOn() {
        XposedHelpers.findAndHookMethod(View.class, "setKeepScreenOn", boolean.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) {
                if (Boolean.FALSE.equals(param.args[0])) {
                    param.args[0] = true;
                }
            }
        });
    }

    private static void applyKeepScreenOn(Activity activity) {
        if (activity == null) {
            return;
        }
        try {
            Window window = activity.getWindow();
            if (window == null) {
                return;
            }
            window.addFlags(KEEP_SCREEN_ON);
            View decorView = window.getDecorView();
            if (decorView != null) {
                decorView.setKeepScreenOn(true);
            }
        } catch (Throwable throwable) {
            XposedBridge.log(TAG + ": failed to apply keep-screen-on: " + throwable);
        }
    }
}
