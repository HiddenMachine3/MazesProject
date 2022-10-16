import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;

public class Main {
    /**
     * java verbosity ==> readability -1 :(
     */

    void start() {
        try {
            File currentJarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File appFolderContainingGetdownFiles = new File(currentJarFile.getParent() + "/app");

            File appFolder = new File(getPlatformDependentAppLocation() + "MazesProject");
            if (appFolder.createNewFile()) System.out.println("AppFolder created successfully");
            else throw new RuntimeException("could not create app folder in " + appFolder.getPath());

            FileUtils.copyDirectory(appFolderContainingGetdownFiles, appFolder);

            File getdownJarFile = new File(appFolder.getAbsolutePath() + "/getdown.jar");
            URLClassLoader loader = new URLClassLoader(new URL[]{getdownJarFile.toURI().toURL()});
            Class<?> mainClass = loader.loadClass("com.threerings.getdown.launcher.GetdownApp");
            Method mainMethod = mainClass.getMethod("main", String[].class);
            try {
                mainMethod.invoke(this, (Object) new String[]{appFolder.getPath()});
                //new Object[]{appFolderContainingGetdownFiles.toString()});
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        } catch (ClassNotFoundException | NoSuchMethodException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    private String getPlatformDependentAppLocation() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.contains("win")) {//windows
            return System.getProperty("user.home") + "/AppData/";
        } else if (OS.contains("nux") || OS.contains("nix") || OS.contains("aix")) {//unix
            return System.getProperty("user.home") + "/.";
        } else if (OS.contains("mac")) {//macos
            return System.getProperty("NSApplicationSupportDirectory") + "/";
        } else {
            throw new RuntimeException("Unsupported Operating System!");
        }
    }

    public static void main(String[] args) {
        new Main().start();
    }
}
