@echo off
title SmartHomeFX - Sistema de Gestion y Automatizacion para Hogares Inteligentes
echo ===================================================================
echo   INICIANDO SMART HOME MANAGEMENT SYSTEM (JavaFX + FXML)
echo ===================================================================
echo.

set JAVA_HOME=C:\Program Files\Java\jdk-17
set PATH=%JAVA_HOME%\bin;C:\Program Files\JetBrains\IntelliJ IDEA 2024.3.4.1\plugins\maven\lib\maven3\bin;%PATH%

echo Compilando y ejecutando aplicacion...
echo.

call mvn clean compile exec:java

if %ERRORLEVEL% NEQ 0 (
    echo.
    echo [ERROR] Hubo un problema al iniciar la aplicacion.
    pause
)

