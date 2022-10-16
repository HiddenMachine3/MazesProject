cd ..
cd ..

::making platform specific package
::#rm -rf out/final_package/MazeApp_linux/*
::rm -rf out/final_package/MazeApp_windows/*
::java -jar "/opt/jar files/packr-all-4.0.0.jar" packaging/packr-config.json

::#Generating digest.txt file
java -classpath "C:\Users\megha\important jars\getdown-core-1.8.7.jar" com.threerings.getdown.tools.Digester out\app

::copy contents of app to serverlocation
rmdir /s /q C:\Users\megha\folderOnServer\app
mkdir C:\Users\megha\folderOnServer\app
xcopy out\app C:\Users\megha\folderOnServer\app /E /Q