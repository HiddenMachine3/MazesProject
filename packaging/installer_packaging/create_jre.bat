@echo off
jdeps -q --print-module-deps --ignore-missing-deps C:\Users\megha\Downloads\package\MazesProject\out\app\MazesProject.jar > tmpFile
set /p modules= < tmpFile 
del tmpFile
jlink --add-modules %modules%,jdk.crypto.ec --output myruntime
pause