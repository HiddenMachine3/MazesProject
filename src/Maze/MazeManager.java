package Maze;

import Maze.IO.Window;
import Maze.IO.graphic.MazeGraphic;
import Maze.Logic.Cell;
import Maze.Logic.Generation.Generator;
import Maze.Logic.Solving.Solver;

import javax.swing.*;
import java.awt.*;

public class MazeManager {

    static Window refreshableWindow;
    Window window;
    MazeGraphic graphic;

    volatile long solve_delay = 1000, gen_delay = 0;
    volatile int width, height;
    volatile Cell[][] maze;
    Solver solver;
    Generator generator;
    SolvingThread solvingThread;
    GeneratingThread generatingThread;

    String solvingAlgorithm = "Tremaux";
    String generatingAlgorithm = "RandomBacktracker";

    //todo:add other algorithm strategeis

    MazeManager() {
        generator = new Generator();
        solver = new Solver();
        graphic = new MazeGraphic();
        window = new Window(graphic, a -> generate(), a -> solve(),
                a -> generatingAlgorithm = (String) ((JComboBox) a.getSource()).getSelectedItem(),
                a -> solvingAlgorithm = (String) ((JComboBox) a.getSource()).getSelectedItem(),
                c -> width = (Integer) ((JSpinner) c.getSource()).getValue(),
                c -> height = (Integer) ((JSpinner) c.getSource()).getValue(),
                c -> solve_delay = (Integer) ((JSpinner) c.getSource()).getValue(),
                c -> gen_delay = (Integer) ((JSpinner) c.getSource()).getValue());//these are just action listeners written with lambda expressions
        refreshableWindow = window;
    }

    public void setData(int width, int height) {
        this.width = width;
        this.height = height;
        maze = newMaze(width, height);

        graphic.setData(maze, width, height, 500, 500);

        displayWindow();
        //displayWinodow()-->calls the setVisible(true) function, refresh()-->calls the repaint() function
        //if the user doesn't do anything after setData(), then the new maze won't be painted correctly
        //we have to have to call the setVisible() function because of changing maze information
        //could use repaint(), but the problem still stays for some reason
    }

    public void generate(Cell[][] grid, int width, int height, long generating_delay) {
        generator.setData(generatingAlgorithm, grid, width, height, generating_delay);
        graphic.setPaintingStrategy(generator.generatingStrategy.getPaintingStrategy());
        generator.generate();
    }

    public void solve(Cell[][] grid, int width, int height, long solving_delay,
                      int sx, int sy, int ex, int ey) {
        solver.setData(solvingAlgorithm, grid, width, height, solving_delay, sx, sy, ex, ey);
        graphic.setPaintingStrategy(solver.solvingStrategy.getPaintingStrategy());
        solver.solve();
    }

    public static Cell[][] newMaze(int width, int height) {
        Cell[][] maze = new Cell[width][height];
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                maze[i][j] = new Cell(false);
        return maze;
    }

    //GUI supporting functions
    public void displayWindow() {
        window.display();
    }

    public void generate() {
        setData(width, height);
        generatingThread = new GeneratingThread();
        generatingThread.start();
    }

    public void solve() {
        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; maze[i][j].filled = false, j++) ;
        solvingThread = new SolvingThread();
        solvingThread.start();
    }

    class GeneratingThread extends Thread {
        @Override
        public void run() {
            generate(maze, width, height, gen_delay);
        }
    }

    class SolvingThread extends Thread {
        @Override
        public void run() {
            solve(maze, width, height, solve_delay, 0, 0, width - 1, height - 1);
        }
    }

    public static void refresh() {
        refreshableWindow.refresh();
    }
}
