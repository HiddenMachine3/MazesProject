package Maze.Logic.Generation;

import Maze.Logic.Cell;
import Maze.Logic.Generation.Strategies.RandomBacktracker;

import java.lang.reflect.InvocationTargetException;

public class Generator {
    public GeneratingStrategy generatingStrategy;

    public void setData(String strategy, Cell[][] grid, int width, int height, long solving_delay) {

        try {
            generatingStrategy = (GeneratingStrategy) (Class.forName("Maze.Logic.Generation.Strategies." + strategy).getConstructor().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
            generatingStrategy = new RandomBacktracker();
        }

        generatingStrategy.setData(grid, width, height, solving_delay);
    }

    public void generate() {
        generatingStrategy.generate();
    }

}