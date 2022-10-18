package Maze;

import javax.swing.*;

//todo:remove middleman.jar
//todo: get a server to deploy application files and database
//todo: launch application after installation
//todo: figure out way to use custom UI for jpackage msi installer ==> "You need an internet connection to
// download the application related files"
public class Main {
    public static void main(String[] args) {

        try {
            UIManager.setLookAndFeel("com.formdev.flatlaf.FlatDarculaLaf");
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            throw new RuntimeException(e);
        }

        MazeManager manager = new MazeManager();
        manager.displayWindow();
        manager.setData(5, 5);
        manager.generate();
        //manager.solve();
    }
    //todo:add DepthFirstSearch

}
