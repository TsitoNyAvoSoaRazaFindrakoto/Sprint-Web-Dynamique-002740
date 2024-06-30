@echo off
cd ../bin/

set "my_path=D:\Studies\MrNaina\libs\myFramework.jar"
set "my_test=D:\Studies\MrNaina\Test_Sprint\lib\myFramework.jar"

if exist "%my_path%" (
    del "%my_path%"
)


if exist "%my_path%" (
    del "%my_test%"
)

jar cf "%my_path%" *
jar cf "%my_test%" *