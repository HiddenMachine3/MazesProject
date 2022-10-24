
#making output dir if not exists
if [ ! -d "output" ]; then
    mkdir output
fi

#deciding appversion based on date and time
appver=$(date +%Y.%m.%d).$(date +"%T")
appver=${appver//[:]/}
echo $appver

#making installer
jpackage --input lib/ \
		 --main-jar getdown.jar \
		 -n MazesProjectApp \
		 --runtime-image myruntime \
		 --linux-shortcut \
		 --icon lib/Resources/Icon.ico \
		 --app-version $appver \
		 --arguments '$APPDIR' \
		 -d output
#you have to use a different app version every time so that it automatically uninstalls the old version, or else you have to manually uninstall the old version