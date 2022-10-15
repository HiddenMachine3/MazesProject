@echo off
set appver=%date:~6,2%.%date:~3,2%.%date:~0,2%%time:~0,4%
set appver=%appver::=%
echo %appver%

rem goto endcomment
jpackage --input lib/ ^
		 --main-jar ZoomAutomator.jar ^
		 -n ZoomAutomator ^
		 --runtime-image myruntime ^
		 --win-dir-chooser ^
		 --win-menu ^
		 --win-shortcut ^
		 --icon lib/Resources/Icon.ico ^
		 --app-version %appver% ^
		 -d output
:endcomment
rem you have to use a different app version every time so that it automatically uninstalls the old version, or else you have to manually uninstall the old version
pause