cd ..
cd ..

#making package
#rm -rf out/final_package/MazeApp_linux/*
#rm -rf out/final_package/MazeApp_windows/*
#java -jar "/opt/jar files/packr-all-4.0.0.jar" packaging/packr-config.json

pwd
#Generating digest.txt file
java -classpath /opt/jar\ files/getdown-core-1.8.7.jar com.threerings.getdown.tools.Digester out/app

#copy contents of app to serverlocation
rm -r /home/hidden-machine/Documents/folderOnServer/app
cp -r out/app /home/hidden-machine/Documents/folderOnServer/
