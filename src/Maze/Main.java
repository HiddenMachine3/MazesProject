package Maze;

import javax.swing.*;

//todo: get a server to deploy application files and database

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
