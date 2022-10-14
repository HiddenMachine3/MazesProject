package Maze.Logic.Solving.Strategies;

import Maze.IO.PaintingStrategies.PaintingStrategy;
import Maze.Logic.Cell;
import Maze.Logic.Solving.SolvingStrategy;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
//import java.awt.geom.
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

import static Maze.IO.graphic.MazeGraphic.screen_height;
import static Maze.IO.graphic.MazeGraphic.screen_width;

public class Tremaux extends SolvingStrategy {
    TremauxCell[][] tcells;

    @Override
    public void initialize() {
        tcells = new TremauxCell[width][height];
        for (int i = 0, j; i < width; i++)
            for (j = 0; j < height; j++) {
                boolean isJunction = isJunction(i, j);
                boolean[] walls = Grid[i][j].walls;
                tcells[i][j] = new TremauxCell(i, j, isJunction, new int[]{walls[0] ? 2 : 0, walls[1] ? 2 : 0, walls[2] ? 2 : 0, walls[3] ? 2 : 0});
            }
        cx = sx;
        cy = sy;
    }

    @Override
    public void solve() {
        Point dir = new Point(0, 0),
                n = new Point(0, -1),
                s = new Point(0, 1),
                e = new Point(1, 0),
                w = new Point(-1, 0);
        Random random = new Random();
        HashMap<Integer, Point> pointDirMap = new HashMap<>(Map.of(
                0, n,
                1, e,
                2, s,
                3, w
        ));

        TremauxCell currentCell;
        int currentDirection;
        currentCell = tcells[cx][cy];
        updateImage();

        while (!(cx == ex && cy == ey)) {//n,e,s,w==>0,1,2,3
            //finding possible directions

            List<Map.Entry<Integer, Integer>> possibleDirs = new LinkedList<>();

            for (int i = 0; i < 4; i++)
                if (currentCell.marks[i] <= 1)
                    possibleDirs.add(Map.entry(i, currentCell.marks[i]));

            possibleDirs.sort(Map.Entry.comparingByValue());

            currentDirection = possibleDirs.get(0).getKey();
            Point direction = pointDirMap.get(currentDirection);

            //moving in direction of currentDirection and putting marks
            currentCell.marks[currentDirection]++;
            cx += direction.x;
            cy += direction.y;
            currentCell = tcells[cx][cy];
            currentCell.marks[(currentDirection + 2) % 4]++;

            updateImage();
        }
        System.out.println("Solved!");
    }

    class TremauxCell extends Cell {
        boolean isJunction;
        int[] marks;//n,e,s,w

        public TremauxCell(int x, int y, boolean isJunction, int[] marks) {
            super(x, y, false);
            this.isJunction = isJunction;
            this.marks = marks;
        }
    }

    boolean isJunction(int i, int j) {
        Cell cell = Grid[i][j];
        //count number of walls
        int numWalls = 0;
        for (int wall = 0; wall < 4; numWalls += cell.walls[wall] ? 1 : 0, wall++) ;

        switch (numWalls) {
            case 0:
            case 1:
                return true;
            case 2:
                for (int wall = 0; wall < 4; wall++) {
                    if (cell.walls[wall] && cell.walls[(wall + 1) % 4])
                        return true;
                }
                return false;
            case 3:
            case 4:
                return false;
        }
        System.err.println("Junction not identified!");
        return false;
    }

    public PaintingStrategy getPaintingStrategy() {
        return new PaintingStrategy() {
            public final int filled = Color.yellow.getRGB();
            public final int current = Color.pink.getRGB();

            final Line2D line = new Line2D.Double(0, 0, 0, 0);
            final Ellipse2D oval = new Ellipse2D.Double(0, 0, 10, 10);
            final BasicStroke stroke = new BasicStroke(1);
            final Font font = new Font("serif", Font.PLAIN, (int) (0.25 * screen_height / height));

            @Override
            public void paint(BufferedImage screen, Graphics gg) {
                Graphics2D g = (Graphics2D) gg;
                g.setFont(font);
                {//Filling the cells
                    for (int i = 0; i < height; i++)
                        for (int j = 0; j < width; j++) {
                            screen.setRGB(j, i, filled);
                        }
                    screen.setRGB(cx, cy, current);
                    g.drawImage(screen, 0, 0, screen_width, screen_height, null);
                }
                {//Drawing the walls and marks
                    g.setColor(Color.black);
                    g.setStroke(stroke);
                    for (double i = 0; i < height; i++)
                        for (double j = 0; j < width; j++) {
                            boolean[] walls = Grid[(int) j][(int) i].walls;
                            int[] marks = tcells[(int) j][(int) i].marks;

                            //((((2j+1/ width)) * screen_width)/2

                            if (walls[0]) {//north
                                line.setLine((j / width) * screen_width, (i / height) * screen_height,
                                        ((j + 1) / width) * screen_width, (i / height) * screen_height);
                                g.draw(line);
                            }
                            if (walls[2]) {//south
                                line.setLine((j / width) * screen_width, ((i + 1) / height) * screen_height,
                                        ((j + 1) / width) * screen_width, ((i + 1) / height) * screen_height);
                                g.draw(line);
                            }
                            if (walls[1]) {//east
                                line.setLine(((j + 1) / width) * screen_width, ((i) / height) * screen_height,
                                        ((j + 1) / width) * screen_width, ((i + 1) / height) * screen_height);
                                g.draw(line);
                            }
                            if (walls[3]) {//west
                                line.setLine(((j) / width) * screen_width, ((i) / height) * screen_height,
                                        ((j) / width) * screen_width, ((i + 1) / height) * screen_height);
                                g.draw(line);
                            }


                            oval.setFrame(0.5 * ((2 * j + 1) / width) * screen_width, (i / height) * screen_height, 10, 10);
                            if (marks[0] == 1) {
                                g.fill(oval);
                            } else if (marks[0] == 2)
                                g.drawString("X", (float) oval.getX(), (float) oval.getY());

                            oval.setFrame(0.5 * ((2 * j + 1) / width) * screen_width, ((i + 1) / height) * screen_height, 10, 10);
                            if (marks[2] == 1) {
                                g.fill(oval);
                            } else if (marks[2] == 2)
                                g.drawString("X", (float) oval.getX(), (float) oval.getY());

                            oval.setFrame(((j + 1) / width) * screen_width, 0.5 * ((2 * i + 1) / height) * screen_height, 10, 10);
                            if (marks[1] == 1) {
                                g.fill(oval);
                            } else if (marks[1] == 2)
                                g.drawString("X", (float) oval.getX(), (float) oval.getY());

                            oval.setFrame((j / width) * screen_width, 0.5 * ((2 * i + 1) / height) * screen_height, 10, 10);
                            if (marks[3] == 1) {
                                g.fill(oval);
                            } else if (marks[3] == 2)
                                g.drawString("X", (float) oval.getX(), (float) oval.getY());
                        }
                }
            }
        };
    }
}
