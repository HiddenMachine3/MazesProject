@echo off
jdeps -q --print-module-deps --ignore-missing-deps lib/ZoomAutomator.jar > tmpFile 
set /p modules= < tmpFile 
del tmpFile
jlink --add-modules %modules%,jdk.crypto.ec --output myruntime
pause