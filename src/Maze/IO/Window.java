package Maze.IO;

import Maze.IO.graphic.MazeGraphic;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionListener;

public class Window extends JFrame {

    public MazeGraphic MazeGraphic;
    public Display display;

    public Window(MazeGraphic MazeGraphic, ActionListener a1, ActionListener a2, ActionListener a3, ActionListener a4,
                  ChangeListener c1, ChangeListener c2, ChangeListener c3, ChangeListener c4) {
        setLayout(new FlowLayout());
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        this.MazeGraphic = MazeGraphic;
        display = new Display(MazeGraphic, a1, a2, a3, a4, c1, c2, c3, c4);
        setContentPane(display.MainPanel);
    }

    public void refresh() {
        display.MainPanel.repaint();
        //display.MazeGraphic.repaint ();
    }

    public void display() {
        setVisible(true);
    }
}
