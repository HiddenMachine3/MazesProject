package main;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {
    /**
     * java verbosity = readability -1 :(
     */


    void start() {
        try {
            File currentJarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File appFolderContainingGetdownFiles = new File(currentJarFile.getParent() + "/app");
            File getdownJarFile = new File(appFolderContainingGetdownFiles.getAbsolutePath() + "/getdown.jar");

            URLClassLoader loader = new URLClassLoader(new URL[]{getdownJarFile.toURI().toURL()});
            Class<?> mainClass = loader.loadClass("com.threerings.getdown.launcher.GetdownApp");
            Method mainMethod = mainClass.getMethod("main", String[].class);

            System.out.println(appFolderContainingGetdownFiles.exists() + " can:"
                    + appFolderContainingGetdownFiles.getCanonicalPath() + " path:"
                    + appFolderContainingGetdownFiles.getPath() + " absolutePath:"
                    + appFolderContainingGetdownFiles.getAbsolutePath() + " ");

            try {
                mainMethod.invoke(this, (Object) new String[]{appFolderContainingGetdownFiles.getPath()});
                //new Object[]{appFolderContainingGetdownFiles.toString()});
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException | NoSuchMethodException | MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
