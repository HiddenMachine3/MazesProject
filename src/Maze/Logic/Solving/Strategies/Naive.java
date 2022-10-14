package Maze.Logic.Solving.Strategies;

import Maze.Logic.Cell;
import Maze.Logic.Solving.SolvingStrategy;

import java.util.ArrayList;

public class Naive extends SolvingStrategy {

    Direction[][] Directions;

    protected class Direction {
        boolean[] dir;

        Direction(boolean[] dir) {
            this.dir = dir;
        }
    }

    @Override
    public void initialize() {
        Directions = new Direction[width][height];
        {
            for (int i = 0; i < width; i++) {
                for (int j = 0; j < height; j++) {
                    Cell cell = Grid[i][j];
                    cell.filled = false;
                    Directions[i][j] = new Direction(new boolean[]{
                            !cell.walls[0], !cell.walls[1],
                            !cell.walls[2], !cell.walls[3]});
                }
            }
        }
        cx = sx;
        cy = sy;
        Grid[cx][cy].filled = true;//filling the starting cell
    }

    final static int[] hor = {0, +1, 0, -1};//north, east, south, west
    final static int[] ver = {-1, 0, +1, 0};//north, east, south, west

    @Override
    public void solve() {
        ArrayList<Integer> available = new ArrayList<>();
        while (true) {
            if (cx == ex && cy == ey)
                break;

            available.clear();

            int lastChoice = 0;
            boolean[] directions = Directions[cx][cy].dir;
            for (int i = 0; i < 4; i++) {
                if (directions[i]) {
                    int newx = cx + hor[i], newy = cy + ver[i];
                    if (newx < width && newx > -1 && newy < height && newy > -1) {

                        if (Grid[newx][newy].filled && !Directions[newx][newy].dir[(i + 2) % 4]) {
                            lastChoice = i;
                            continue;//implies that if the cell is already traversed previously,
                        }// and going in the opposite direction is banned, then you should consider this option last


                        available.add(i);
                    } else
                        System.out.println(newx + " " + newy);
                }
            }

            Cell prev = Grid[cx][cy];
            if (available.size() == 0) {
                available.add(lastChoice);//this signifies that the taken this cell is deleted from the path
                prev.filled = false;
            }

            int chosenDir = available.get((int) (Math.random() * available.size()));
            Directions[cx][cy].dir[chosenDir] = false;//preventing going in the same direction in the same cell twice

            cx = cx + hor[chosenDir];
            cy = cy + ver[chosenDir];
            Cell chosen = Grid[cx][cy];

            chosen.filled = true;

            updateImage();
        }
        System.out.println("Finished Solving");
    }
}
