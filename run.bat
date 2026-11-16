@echo off
chcp 65001 >nul
cd /d "%~dp0"

set "GAME_JAVA=java"

if exist "%USERPROFILE%\.jdks\temurin-17.0.21\bin\java.exe" (
    set "GAME_JAVA=%USERPROFILE%\.jdks\temurin-17.0.21\bin\java.exe"
)

"%GAME_JAVA%" -Dfile.encoding=UTF-8 -jar "target\scenario-manager-1.0-SNAPSHOT.jar"

pause