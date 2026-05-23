# NeverSleep

LSPosed module: keep the screen awake for apps selected in LSPosed scope.

Module package name: `io.c4.ns`.

- Package name: `io.c4.ns`
- First recommended scope: `com.luna.music`
- Version: `1.0.0`

## How it works

Inside each scoped app process, the module:

1. Hooks `Activity.onCreate`, `Activity.onResume`, and `Activity.onWindowFocusChanged(true)`.
2. Adds `WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON` to the current Activity window.
3. Sets the window decor view `keepScreenOn` property to `true`.
4. Prevents the app from clearing `FLAG_KEEP_SCREEN_ON` through `Window.clearFlags()`.
5. Forces `View.setKeepScreenOn(false)` calls back to `true`.

This avoids changing global Android sleep policy. The behavior is limited to LSPosed-selected scope apps.

## Build

```bash
./gradlew :app:assembleDebug
```

The debug-signed APK will be generated at:

```text
app/build/outputs/apk/debug/app-debug.apk
```

## Usage

1. Install the APK.
2. Open LSPosed.
3. Enable `NeverSleep`.
4. Select the target scope. Recommended first scope: `com.luna.music`.
5. Force stop and reopen the target app.

## Notes

- The project includes `assets/xposed_scope` with `com.luna.music` as a recommended scope hint for LSPosed versions that read it.
- The actual runtime scope is controlled by LSPosed. If you select more apps, the hook applies to those apps too.


## GitHub Actions

The repository includes `.github/workflows/build.yml`.

- Every push to `main`/`master` builds a debug-signed APK.
- The workflow uploads `NeverSleep-debug.apk` as the `NeverSleep-debug` artifact.
