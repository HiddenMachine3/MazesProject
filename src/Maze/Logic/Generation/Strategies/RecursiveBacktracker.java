package Maze.Logic.Generation.Strategies;

import Maze.Logic.Cell;
import Maze.Logic.Generation.GeneratingStrategy;

import java.awt.*;
import java.util.*;

public class RecursiveBacktracker extends GeneratingStrategy {
    /**
     * Given a current cell as a parameter
     * Mark the current cell as visited
     * While the current cell has any unvisited neighbour cells
     * * * Choose one of the unvisited neighbours
     * * * Remove the wall between the current cell and the chosen cell
     * * * Invoke the routine recursively for the chosen cell
     * which is invoked once for any initial cell in the area.
     */
    @Override
    public void generate() {
        //initializing
        for (int i = 0, j; i < width; i++)
            for (j = 0; j < height; j++) {
                Grid[i][j].set(i, j, false);
                Grid[i][j].visited = false;
            }


        Cell currentCell = Grid[0][0];
//        System.out.println(n+" "+s+" " + e+" " +w);
        recursive_back(currentCell);
        updateImage();
        System.out.println("Finished Generating");
    }

    static Point n = new Point(0, -1),
            s = new Point(0, 1),
            e = new Point(1, 0),
            w = new Point(-1, 0);
    Point dir = new Point(0, 0);
    static HashMap<Point, Integer> pointDirMap = new HashMap<>(Map.of(
            n, 0,
            e, 1,
            s, 2,
            w, 3
    ));

    private void recursive_back(Cell currentCell) {
        currentCell.filled = true;

        currentCell.visited = true;
        ArrayList<Point> possibleDirs = new ArrayList<>();

        int x = currentCell.x, y = currentCell.y;
//        System.out.println(x + " "+y);
        {//finding possible directions
            if (x != 0 && !Grid[x - 1][y].visited)
                possibleDirs.add(w);
            if (y != 0 && !Grid[x][y - 1].visited)
                possibleDirs.add(n);
            if (x != (width - 1) && !Grid[x + 1][y].visited)
                possibleDirs.add(e);
            if (y != (height - 1) && !Grid[x][y + 1].visited)
                possibleDirs.add(s);
        }
//        System.out.println(n+" "+s+" " + e+" " +w);
//        System.out.println(possibleDirs + " "+ width + " "+height + " "+((y != (height - 1) && !Grid[x][y + 1].visited)));
        Collections.shuffle(possibleDirs);
        while (!possibleDirs.isEmpty()) {
            //choosing nextCell
            dir.setLocation(possibleDirs.remove(possibleDirs.size() -1));
            Cell nextCell = Grid[x + dir.x][y + dir.y];
            if (!nextCell.visited) {// checking if already visited
                {//breaking walls
                    int wallToBreak1 = pointDirMap.get(dir);
                    currentCell.walls[wallToBreak1] = false;

                    int wallToBreak2 = (wallToBreak1 + 2) % 4;
                    nextCell.walls[wallToBreak2] = false;
                }

                updateImage();
                recursive_back(nextCell);
            }
        }
    }
}