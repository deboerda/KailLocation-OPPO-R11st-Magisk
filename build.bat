@echo off
setlocal EnableExtensions
chcp 65001 >nul
title KailLocation Debug Build

:: AGP/AIDL cannot reliably generate source files under this project's
:: non-ASCII Windows path.  Build from an ASCII-only working copy instead.
set "SOURCE_DIR=%~dp0"
:: %~dp0 always ends in a backslash; remove it before passing the path to
:: robocopy inside quotes (otherwise its quote parser treats it ambiguously).
set "SOURCE_DIR=%SOURCE_DIR:~0,-1%"
set "BUILD_DIR=C:\tmp\kail_location_build"
set "JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-21.0.12.8-hotspot"
set "ANDROID_HOME=C:\tmp\android-sdk"
set "ANDROID_SDK_ROOT=%ANDROID_HOME%"
set "GRADLE_USER_HOME=C:\tmp\gradle-cache"
set "TEMP=C:\tmp\agp-temp"
set "TMP=C:\tmp\agp-temp"
set "PATH=%JAVA_HOME%\bin;%PATH%"

if not exist "%JAVA_HOME%\bin\java.exe" (
    echo [ERROR] JDK 21 was not found: %JAVA_HOME%
    exit /b 1
)
if not exist "%ANDROID_HOME%\platforms" (
    echo [ERROR] Android SDK was not found: %ANDROID_HOME%
    exit /b 1
)

echo Syncing source to %BUILD_DIR%
robocopy "%SOURCE_DIR%" "%BUILD_DIR%" /E /XD ".git" ".gradle" "build" /NFL /NDL /NJH /NJS /NP
if errorlevel 8 (
    echo [ERROR] Source synchronization failed.
    exit /b 1
)

pushd "%BUILD_DIR%"
call gradlew.bat --stop >nul 2>&1
call gradlew.bat assembleDebug
set "BUILD_RESULT=%ERRORLEVEL%"
popd

if not "%BUILD_RESULT%"=="0" (
    echo [ERROR] Debug build failed. APK was not copied back.
    exit /b %BUILD_RESULT%
)

set "APK_SOURCE=%BUILD_DIR%\app\build\outputs\apk\debug\KailLocation-open-beta-debug.apk"
set "APK_DEST=%SOURCE_DIR%app\build\outputs\apk\debug\KailLocation-open-beta-debug.apk"
if not exist "%APK_SOURCE%" (
    echo [ERROR] Build completed but the expected APK was not found.
    exit /b 1
)
if not exist "%SOURCE_DIR%app\build\outputs\apk\debug" mkdir "%SOURCE_DIR%app\build\outputs\apk\debug"
copy /Y "%APK_SOURCE%" "%APK_DEST%" >nul

echo.
echo Build successful.
echo APK: %APK_DEST%
exit /b 0
