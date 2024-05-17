@echo off
cd ../bin/

set "my_path=D:\Studies\MrNaina\libs\myFramework.jar"

if exist "%my_path%" (
    del "%my_path%"
)

jar cf "%my_path%" *