package Maze.Logic.Solving;

import Maze.Logic.Cell;
import Maze.Logic.Solving.Strategies.Naive;

public class Solver {

    public SolvingStrategy solvingStrategy;

    public void setData(String strategy, Cell[][] grid, int width, int height,
                        long solving_delay, int sx, int sy, int ex, int ey) {
        try {
            solvingStrategy = (SolvingStrategy) (Class.forName("Maze.Logic.Solving.Strategies." + strategy).getConstructor().newInstance());
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error");
            solvingStrategy = new Naive();
        }
        solvingStrategy.setData(grid, width, height, solving_delay, sx, sy, ex, ey);
    }

    public void solve() {
        solvingStrategy.solve();
    }

}
