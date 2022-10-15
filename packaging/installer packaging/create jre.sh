if [ -d "myruntime" ]; then
    rm -r -f myruntime
fi

jdeps -q --print-module-deps --ignore-missing-deps "/home/hidden-machine/Java Projects on Linux/MazesProject/out/app/MazesProject.jar" > tmpFile
modules=$(cat tmpFile)
rm tmpFile
jlink --add-modules $modules,jdk.crypto.ec --output myruntime

echo "created jre"