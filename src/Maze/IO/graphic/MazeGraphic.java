package Maze.IO.graphic;


import Maze.IO.PaintingStrategies.PaintingStrategy;
import Maze.Logic.Cell;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;

public class MazeGraphic extends JComponent {

    Cell[][] Grid;
    public static int width, height;
    BufferedImage screen;
    public static int screen_width, screen_height;

    public void setData(Cell[][] Grid, int width, int height, int screen_width, int screen_height) {
        MazeGraphic.width = width;
        MazeGraphic.height = height;
        MazeGraphic.screen_width = screen_width;
        MazeGraphic.screen_height = screen_height;
        this.Grid = Grid;
        screen = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Dimension size = new Dimension(screen_width + 5, screen_height + 5);
        setSize(size);
        setPreferredSize(size);
    }

    PaintingStrategy currentPaintingStrategy = new PaintingStrategy();

    public void setPaintingStrategy(PaintingStrategy ps) {
        if (currentPaintingStrategy != null)
            currentPaintingStrategy = null;
        currentPaintingStrategy = ps;
    }

    @Override
    public void paintComponent(Graphics gg) {
        //currentPaintingStrategy.paint(g);
        currentPaintingStrategy.paint(screen, gg);
    }

}
