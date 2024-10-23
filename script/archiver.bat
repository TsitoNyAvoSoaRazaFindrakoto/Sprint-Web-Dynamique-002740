@echo off
cd ../bin/

set "my_path=D:\Studies\L2\MrNaina\libs\myFramework.jar"
set "my_test=D:\Studies\L2\MrNaina\Test_Sprint\lib\myFramework.jar"

if exist "%my_path%" (
    del "%my_path%"
)


if exist "%my_path%" (
    del "%my_test%"
)

jar cf "%my_path%" *
jar cf "%my_test%" *