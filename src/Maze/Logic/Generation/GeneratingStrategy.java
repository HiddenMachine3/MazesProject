package Maze.Logic.Generation;

import Maze.IO.PaintingStrategies.PaintingStrategy;
import Maze.IO.graphic.MazeGraphic;
import Maze.Logic.Cell;
import Maze.MazeManager;

import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

import static Maze.IO.graphic.MazeGraphic.screen_height;
import static Maze.IO.graphic.MazeGraphic.screen_width;

public abstract class GeneratingStrategy {

    protected long delay;

    protected Cell[][] Grid;
    protected int width, height;

    public void setData(Cell[][] Grid, int width, int height, long solving_delay) {
        this.Grid = Grid;
        this.delay = solving_delay;
        this.width = width;
        this.height = height;
        if (Grid == null)
            for (int i = 0; i < width; i++)
                for (int j = 0; j < height; j++)
                    Grid[i][j] = new Cell(false);
        initialize();
    }

    public void initialize() {
    }

    public abstract void generate();

    public void updateImage() {
        MazeManager.refresh();
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public PaintingStrategy getPaintingStrategy() {
        return new PaintingStrategy() {
            public final int filled = Color.yellow.getRGB();
            public final int unfilled = Color.darkGray.getRGB(); //black.getRGB();

            final Line2D line = new Line2D.Double(0, 0, 0, 0);
            final BasicStroke stroke = new BasicStroke(5);

            @Override
            public void paint(BufferedImage screen, Graphics gg) {
                Graphics2D g = (Graphics2D) gg;
                {//Filling the cells
                    for (int i = 0; i < height; i++)
                        for (int j = 0; j < width; j++) {
                            screen.setRGB(j, i, Grid[j][i].filled ? filled : unfilled);
                        }
                    g.drawImage(screen, 0, 0, screen_width, screen_height, null);
                }
                {//Drawing the walls
                    g.setColor(Color.black);
                    g.setStroke(stroke);
                    for (double i = 0; i < height; i++)
                        for (double j = 0; j < width; j++) {
                            boolean[] walls = Grid[(int) j][(int) i].walls;
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
                        }
                }
            }
        };
    }
}
