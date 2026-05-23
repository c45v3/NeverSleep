#!/usr/bin/env bash
set -euo pipefail
DIR="$(cd "$(dirname "$0")" && pwd)"
GRADLE_VERSION="8.10.2"
if command -v gradle >/dev/null 2>&1; then
  exec gradle "$@"
fi
if ! command -v java >/dev/null 2>&1; then
  echo "Java is required to build this Android project. Install JDK 17+, then rerun ./gradlew." >&2
  exit 1
fi
DIST="$DIR/.gradle-local/gradle-$GRADLE_VERSION/bin/gradle"
if [ ! -x "$DIST" ]; then
  mkdir -p "$DIR/.gradle-local"
  ZIP="$DIR/.gradle-local/gradle-$GRADLE_VERSION-bin.zip"
  URL="https://services.gradle.org/distributions/gradle-$GRADLE_VERSION-bin.zip"
  echo "Downloading Gradle $GRADLE_VERSION..." >&2
  curl -L "$URL" -o "$ZIP"
  unzip -q "$ZIP" -d "$DIR/.gradle-local"
fi
exec "$DIST" "$@"
