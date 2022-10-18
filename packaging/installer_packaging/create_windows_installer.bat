@echo off
set appver=%date:~7,4%.%date:~3,2%.%date:~0,2%.%time:~0,5%
set appver=%appver::=%
set appver=%appver: =0%
echo %appver%

rem goto endcomment
jpackage --input lib/ ^
		 --main-jar getdown.jar ^
		 -n MazesProject ^
		 --runtime-image myruntime ^
		 --icon lib/Resources/Icon.ico ^
         --app-version %appver% ^
		 --win-dir-chooser ^
		 --win-menu ^
		 --win-shortcut ^
		 --win-per-user-install ^
		 -d output
:endcomment
rem you have to use a different app version every time so that it automatically uninstalls the old version, or else you have to manually uninstall the old version
pause