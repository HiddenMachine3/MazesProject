package Maze.Logic.Generation.Strategies;

import Maze.Logic.Cell;
import Maze.Logic.Generation.GeneratingStrategy;

import java.awt.*;
import java.util.*;

public class RandomBacktracker extends GeneratingStrategy {

    @Override
    public void generate() {
        //initializing
        for (int i = 0, j; i < width; i++)
            for (j = 0; j < height; j++) {
                Grid[i][j].set(i, j, false);
            }
        Stack<Cell> history = new Stack<>();
        Cell currentCell;
        ArrayList<Point> possibleDirs = new ArrayList<>();
        Point dir = new Point(0, 0),
                n = new Point(0, -1),
                s = new Point(0, 1),
                e = new Point(1, 0),
                w = new Point(-1, 0);
        Random random = new Random();
        HashMap<Point, Integer> pointDirMap = new HashMap<>(Map.of(
                n, 0,
                e, 1,
                s, 2,
                w, 3
        ));


        currentCell = Grid[0][0];
        currentCell.visited = true;
        history.push(currentCell);

        while (!history.empty()) {
            currentCell = history.pop();
            currentCell.filled = true;
            int x = currentCell.x, y = currentCell.y;

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

            Collections.shuffle(possibleDirs);

            if (!possibleDirs.isEmpty()) {
                history.push(currentCell);
                //choosing nextCell
                dir.setLocation(possibleDirs.remove(possibleDirs.size() -1));
                Cell nextCell = Grid[x + dir.x][y + dir.y];

                {//breaking walls
                    int wallToBreak1 = pointDirMap.get(dir);
                    currentCell.walls[wallToBreak1] = false;

                    int wallToBreak2 = (wallToBreak1 + 2) % 4;
                    nextCell.walls[wallToBreak2] = false;
                }

                nextCell.visited = true;
                history.push(nextCell);
            }

            possibleDirs.clear();
            updateImage();
        }
        System.out.println("Finished Generating");
    }
}