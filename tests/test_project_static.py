from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]

def read(rel):
    return (ROOT / rel).read_text(encoding="utf-8")

def test_manifest_declares_expected_package_and_xposed_metadata():
    manifest = read("app/src/main/AndroidManifest.xml")
    assert "android:name="xposedmodule"" in manifest
    assert "android:name="xposeddescription"" in manifest
    assert "android:name="xposedminversion"" in manifest
    assert "android:name="io.c4.MainActivity"" in manifest

def test_gradle_uses_requested_application_id():
    build = read("app/build.gradle")
    assert "namespace 'io.c4.ns'" in build
    assert "applicationId 'io.c4.ns'" in build
    assert "compileOnly 'de.robv.android.xposed:api:82'" in build

def test_xposed_entrypoint_and_recommended_scope():
    assert read("app/src/main/assets/xposed_init").strip() == "io.c4.ns"
    assert read("app/src/main/assets/xposed_scope").strip() == "com.luna.music"

def test_hook_contains_core_keep_awake_intercepts():
    hook = read("app/src/main/java/io/c4/ns.java")
    for needle in [
        "IXposedHookLoadPackage",
        "Activity.class, "onCreate"",
        "Activity.class, "onResume"",
        "Activity.class, "onWindowFocusChanged"",
        "Window.class, "clearFlags"",
        "View.class, "setKeepScreenOn"",
        "FLAG_KEEP_SCREEN_ON",
        "decorView.setKeepScreenOn(true)",
    ]:
        assert needle in hook


def test_xposed_entry_does_not_clash_with_activity_package():
    assert (ROOT / "app/src/main/java/io/c4/ns.java").exists()
    assert not (ROOT / "app/src/main/java/io/c4/ns/MainActivity.java").exists()
    assert "package io.c4;" in read("app/src/main/java/io/c4/MainActivity.java")
