@echo off
cd ../bin/

if exist "D:\Studies\MrNaina\libs\myFramework.jar" (
    del "D:\Studies\MrNaina\libs\myFramework.jar"
)

jar cf "D:\Studies\MrNaina\libs\myFramework.jar" *