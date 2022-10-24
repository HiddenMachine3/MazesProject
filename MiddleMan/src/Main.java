import javax.swing.*;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Arrays;

public class Main {
    /**
     * java verbosity ==> readability -1 :(
     */

    void start() {
        try {
            File currentJarFile = new File(Main.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            File appFolderContainingGetdownFiles = new File(currentJarFile.getParent());
            File getdownJarFile = new File(appFolderContainingGetdownFiles.getAbsolutePath() + "/getdown.jar");

            if (getdownJarFile.exists()) {
                URLClassLoader loader = new URLClassLoader(new URL[]{getdownJarFile.toURI().toURL()});
                Class<?> mainClass = loader.loadClass("com.threerings.getdown.launcher.GetdownApp");
                Method mainMethod = mainClass.getMethod("main", String[].class);
                try {
                    mainMethod.invoke(this, (Object) new String[]{appFolderContainingGetdownFiles.getPath()});
                    //new Object[]{appFolderContainingGetdownFiles.toString()});
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                throw new RuntimeException("file does not exist! " + getdownJarFile.getPath());
            }
        } catch (ClassNotFoundException | NoSuchMethodException | URISyntaxException | IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        String string = Arrays.toString(args);

        JFrame frame = new JFrame(string);

        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        //new Main().start();
    }
}
