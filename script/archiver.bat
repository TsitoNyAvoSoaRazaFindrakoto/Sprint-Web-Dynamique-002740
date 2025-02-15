@echo off
cd ../bin/

REM Load environment variables from .env file
if exist "../.env" (
    for /F "usebackq tokens=1* delims==" %%A in ("../.env") do (
        set "%%A=%%B"
    )
) else (
    echo .env file not found.
    exit /b 1
)

REM delete previous jars if they exist
if exist "%MY_PATH%" (
    del "%MY_PATH%"
)
if exist "%MY_TEST%" (
    del "%MY_TEST%"
)

jar cf "%MY_PATH%" *
jar cf "%MY_TEST%" *