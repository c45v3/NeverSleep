# NoSleepScope

LSPosed module: keep the screen awake for apps selected in LSPosed scope.

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
./gradlew :app:assembleRelease
```

The APK will be generated at:

```text
app/build/outputs/apk/release/app-release-unsigned.apk
```

For local debug install:

```bash
./gradlew :app:assembleDebug
```

## Usage

1. Install the APK.
2. Open LSPosed.
3. Enable `NoSleepScope`.
4. Select the target scope. Recommended first scope: `com.luna.music`.
5. Force stop and reopen the target app.

## Notes

- The project includes `assets/xposed_scope` with `com.luna.music` as a recommended scope hint for LSPosed versions that read it.
- The actual runtime scope is controlled by LSPosed. If you select more apps, the hook applies to those apps too.


## GitHub Actions

The repository includes `.github/workflows/build.yml`.

- Every push to `main`/`master` builds debug and unsigned release APKs.
- The workflow uploads APKs as the `NoSleepScope-apks` artifact.
- Pushing a tag like `v1.0.0` also creates a GitHub Release and attaches the APKs plus `SHA256SUMS.txt`.

Release from a machine with push permission:

```bash
git tag v1.0.0
git push origin main --tags
```
