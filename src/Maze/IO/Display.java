package Maze.IO;

import Maze.IO.graphic.MazeGraphic;
import Maze.Logic.Cell;
import com.google.common.reflect.ClassPath;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;


public class Display {
    public MazeGraphic MazeGraphic;
    public JPanel MainPanel;
    public JButton generateButton;
    public JButton solveButton;
    public JPanel optionPanel;
    private JPanel canvasPanel;
    private JSpinner solve_delay;
    private JSpinner width;
    private JSpinner height;
    private JSpinner gen_delay;
    private JComboBox solvingAlg;
    private JComboBox generatingAlg;

    public Display(MazeGraphic mazeGraphic,
                   ActionListener generateListener, ActionListener solveListener,
                   ActionListener generateAlgorithmListener, ActionListener solveAlgorithmListener,
                   ChangeListener widthListener, ChangeListener heightListener,
                   ChangeListener solveDelayListener, ChangeListener genDelayListener
    ) {
        this.MazeGraphic = mazeGraphic;//has to take an object at the start because gui's are annoying.
        generateButton.addActionListener(generateListener);
        solveButton.addActionListener(solveListener);
        width.addChangeListener(widthListener);
        height.addChangeListener(heightListener);
        solve_delay.addChangeListener(solveDelayListener);
        gen_delay.addChangeListener(genDelayListener);
        generatingAlg.addActionListener(generateAlgorithmListener);
        solvingAlg.addActionListener(solveAlgorithmListener);

        width.setValue(5);
        height.setValue(5);
        solve_delay.setValue(100);
        gen_delay.setValue(0);

        solvingStrategies().forEach(solvingAlg::addItem);
    }

    public ArrayList<String> solvingStrategies() {
        ArrayList<String> classes = new ArrayList<>();
        final ClassLoader loader = Thread.currentThread().getContextClassLoader();

        try {
            for (ClassPath.ClassInfo info : ClassPath.from(loader).getTopLevelClasses()) {
                if (info.getName().startsWith("Maze.Logic.Solving.Strategies.")) {
                    final Class<?> clazz = info.load();
                    classes.add(clazz.getSimpleName());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return classes;
    }

    public void setData(MazeGraphic mazeGraphic, ActionListener generateListener, ActionListener solveListener) {
        this.MazeGraphic = mazeGraphic;
        generateButton.addActionListener(generateListener);
        solveButton.addActionListener(solveListener);
    }

    public void setData(ActionListener generateListener, ActionListener solveListener) {
        generateButton.addActionListener(generateListener);
        solveButton.addActionListener(solveListener);
    }

    private void createUIComponents() {
        //Maze.Logic.Solving.Strategies.
        //canvasPanel = new JPanel();
    }
}
