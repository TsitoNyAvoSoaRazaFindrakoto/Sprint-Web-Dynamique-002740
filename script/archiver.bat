@echo off
cd ../bin/

set "my_path=D:\Studies\L3\MrNaina\libs\myFramework.jar"
set "my_test=D:\Studies\L3\MrNaina\TripZip\lib\myFramework.jar"

if exist "%my_path%" (
    del "%my_path%"
)


if exist "%my_path%" (
    del "%my_test%"
)

jar cf "%my_path%" *
jar cf "%my_test%" *