if [ -d "myruntime" ]; then
    rm -r -f myruntime
fi

echo test
#jarLoc="/home/hidden-machine/Java Projects on Linux/MazesProject/out/app/MazesProject.jar"
jarLoc="C:\Users\megha\Downloads\package\MazesProject\out\app\MazesProject.jar"

jdeps -q --print-module-deps --ignore-missing-deps $jarLoc > tmpFile
modules=$(cat tmpFile)
rm tmpFile
jlink --add-modules $modules,jdk.crypto.ec --output myruntime

echo "created jre"

read -n1 -r -p "Press any key to continue..." key