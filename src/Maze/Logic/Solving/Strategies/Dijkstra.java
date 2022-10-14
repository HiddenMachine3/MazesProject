package Maze.Logic.Solving.Strategies;

import Maze.IO.PaintingStrategies.PaintingStrategy;
import Maze.Logic.Cell;
import Maze.Logic.Solving.SolvingStrategy;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.image.BufferedImage;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

import static Maze.IO.graphic.MazeGraphic.screen_height;
import static Maze.IO.graphic.MazeGraphic.screen_width;

public class Dijkstra extends SolvingStrategy {

    Set<DijkstraCell> unvisited;
    SimpleGraph<DijkstraCell, DefaultEdge> graph;
    DijkstraCell[][] DCells;
    DijkstraCell currentNode;

    @Override
    public void initialize() {
        graph = new SimpleGraph<>(DefaultEdge.class);
        unvisited = new HashSet<>();
        DCells = new DijkstraCell[width][height];
        //constructing the graph
        for (int i = 0, j; i < width; i++)
            for (j = 0; j < height; j++) {
                DCells[i][j] = new DijkstraCell(i, j);
                graph.addVertex(DCells[i][j]);
                unvisited.add(DCells[i][j]);
            }
        for (int i = 0, j; i < width; i++)
            for (j = 0; j < height; j++) {
                boolean[] walls = Grid[i][j].walls;

                if (!walls[0]) graph.addEdge(DCells[i][j], DCells[i][j - 1]);
                if (!walls[2]) graph.addEdge(DCells[i][j], DCells[i][j + 1]);
                if (!walls[1]) graph.addEdge(DCells[i][j], DCells[i + 1][j]);
                if (!walls[3]) graph.addEdge(DCells[i][j], DCells[i - 1][j]);
            }

    }

    @Override
    public void solve() {
        DijkstraCell endNode = DCells[ex][ey];

        cx = sx;
        cy = sy;

        currentNode = DCells[cx][cy];
        currentNode.distance = 0;

        updateImage();

        while (!endNode.visited) {//n,e,s,w==>0,1,2,3
            cx = currentNode.x;
            cy = currentNode.y;
            //int i = 0, j = 0;
            for (DefaultEdge neighbourEdge : graph.edgesOf(currentNode)) {
                DijkstraCell targetCell = graph.getEdgeTarget(neighbourEdge);
                if (targetCell.equals(currentNode)) targetCell = graph.getEdgeSource(neighbourEdge);
                //i++;
                if ((!targetCell.visited) && !targetCell.equals(currentNode)) {
                    //j++;
                    if (targetCell.distance > currentNode.distance + 1) {
                        targetCell.distance = currentNode.distance + 1;
                        targetCell.prev = currentNode;
                    }
                    //System.out.println("New target cell distance: " + targetCell.distance);
                }
            }
            //System.out.println("Edges : " + i + " unvisitedN : " + j);
            currentNode.visited = true;
            unvisited.remove(currentNode);

            currentNode = getNodeWithLeastDist(unvisited);
            //System.out.println("current cell distance : " + currentNode.distance);
            updateImage();
        }
        currentNode = endNode;
        updateImage();
        System.out.println("Solved!");
    }

    DijkstraCell getNodeWithLeastDist(Set<DijkstraCell> dSet) {
        System.out.println(dSet.size());
        return dSet.stream().min(Comparator.comparingInt(o -> o.distance)).get();
    }

    class DijkstraCell extends Cell {
        boolean visited = false;
        int distance = Integer.MAX_VALUE - 1;
        DijkstraCell prev = null;

        public DijkstraCell(int x, int y) {
            super(x, y, false);

        }

    }

    public PaintingStrategy getPaintingStrategy() {
        return new PaintingStrategy() {
            public final int visited = Color.yellow.getRGB();
            public final int unvisited = Color.gray.getRGB();
            public final int current = Color.pink.getRGB();
            public final int shortest = new Color(66, 26, 137).getRGB();

            final Line2D line = new Line2D.Double(0, 0, 0, 0);
            final Ellipse2D oval = new Ellipse2D.Double(0, 0, 10, 10);
            final BasicStroke thickStroke = new BasicStroke(5);
            final BasicStroke thinStroke = new BasicStroke(1);
            final Font font = new Font("serif", Font.PLAIN, (int) (0.25 * screen_height / height));

            @Override
            public void paint(BufferedImage screen, Graphics gg) {
                Graphics2D g = (Graphics2D) gg;
                g.setFont(font);
                {//Filling the cells
                    for (int i = 0; i < width; i++)
                        for (int j = 0; j < height; j++) {
                            screen.setRGB(i, j, DCells[i][j].visited ? visited : unvisited);
                        }
                    {//Filling the shortest path
                        DijkstraCell prev = currentNode;//getNodeWithLeastDist(Dijkstra.this.unvisited);
                        while (prev != null) {
                            int x = prev.x, y = prev.y;
                            screen.setRGB(x, y, shortest);
                            prev = prev.prev;
                        }
                    }

                    screen.setRGB(Dijkstra.this.currentNode.x, Dijkstra.this.currentNode.y, current);
                    g.drawImage(screen, 0, 0, screen_width, screen_height, null);

                }
                {//Drawing the walls
                    g.setColor(Color.black);
                    g.setStroke(thinStroke);
                    for (double j = 0, halfCellX = 0.5 * screen_width / width, halfCellY = 0.5 * screen_height / height; j < height; j++)
                        for (double i = 0; i < width; i++) {
                            g.drawString(String.valueOf(Math.min(100, DCells[(int) i][(int) j].distance)), (int) (i * screen_width / width + halfCellX), (int) (j * screen_height / height + halfCellY));
                            boolean[] walls = Grid[(int) i][(int) j].walls;
                            //int[] marks = tcells[(int) j][(int) i].marks;

                            //((((2j+1/ width)) * screen_width)/2
                            g.setColor(Color.black);
                            g.setStroke(thickStroke);
                            if (walls[0]) {//north
                                line.setLine((i / width) * screen_width, (j / height) * screen_height,
                                        ((i + 1) / width) * screen_width, (j / height) * screen_height);
                                g.draw(line);
                            }
                            if (walls[2]) {//south
                                line.setLine((i / width) * screen_width, ((j + 1) / height) * screen_height,
                                        ((i + 1) / width) * screen_width, ((j + 1) / height) * screen_height);
                                g.draw(line);
                            }
                            if (walls[1]) {//east
                                line.setLine(((i + 1) / width) * screen_width, ((j) / height) * screen_height,
                                        ((i + 1) / width) * screen_width, ((j + 1) / height) * screen_height);
                                g.draw(line);
                            }
                            if (walls[3]) {//west
                                line.setLine(((i) / width) * screen_width, ((j) / height) * screen_height,
                                        ((i) / width) * screen_width, ((j + 1) / height) * screen_height);
                                g.draw(line);
                            }


                            g.setColor(Color.green);
                            g.setStroke(thinStroke);
                            //checking if the edges are properly existing between graph nodes
                            if (j != 0 && graph.getEdge(DCells[(int) i][(int) j], DCells[(int) i][(int) (j - 1)]) != null) {// walls[0]) {//north
                                line.setLine((i / width) * screen_width, (j / height) * screen_height,
                                        ((i + 1) / width) * screen_width, (j / height) * screen_height);
                                g.draw(line);
                            }
                            if (j != (height - 1) && graph.getEdge(DCells[(int) i][(int) j], DCells[(int) i][(int) (j + 1)]) != null) {// walls[2]) {//south
                                line.setLine((i / width) * screen_width, ((j + 1) / height) * screen_height,
                                        ((i + 1) / width) * screen_width, ((j + 1) / height) * screen_height);
                                g.draw(line);
                            }
                            if (i != (width - 1) && graph.getEdge(DCells[(int) i][(int) j], DCells[(int) (i + 1)][(int) j]) != null) {//walls[1]) {//east
                                line.setLine(((i + 1) / width) * screen_width, ((j) / height) * screen_height,
                                        ((i + 1) / width) * screen_width, ((j + 1) / height) * screen_height);
                                g.draw(line);
                            }
                            if (i != 0 && graph.getEdge(DCells[(int) i][(int) j], DCells[(int) (i - 1)][(int) j]) != null) {//walls[3]) {//west
                                line.setLine(((i) / width) * screen_width, ((j) / height) * screen_height,
                                        ((i) / width) * screen_width, ((j + 1) / height) * screen_height);
                                g.draw(line);
                            }


                        }
                }

            }
        };
    }
}

/*
* g.setColor(Color.green);
                            g.setStroke(thinStroke);
                            //DijkstraCell dcell = DCells[(int) i][(int) j];
                            if (i != 0) //&& !cell.walls[3])//not west
                                if (graph.getEdge(DCells[(int) i][(int) j], DCells[(int) (i - 1)][(int) j]) != null) {
                                    line.setLine(((j) / width) * screen_width, ((i) / height) * screen_height,
                                            ((j) / width) * screen_width, ((i + 1) / height) * screen_height);
                                    g.draw(line);
                                }
                            if (i != (width - 1)) //&& !cell.walls[1])//not east
                                if (graph.getEdge(DCells[(int) i][(int) j], DCells[(int) (i + 1)][(int) j]) != null) {
                                    line.setLine(((j + 1) / width) * screen_width, ((i) / height) * screen_height,
                                            ((j + 1) / width) * screen_width, ((i + 1) / height) * screen_height);
                                    g.draw(line);
                                }
                            if (j != 0) //&& !cell.walls[0])//not north
                                if (graph.getEdge(DCells[(int) i][(int) j], DCells[(int) i][(int) (j - 1)]) != null) {
                                    line.setLine((j / width) * screen_width, (i / height) * screen_height,
                                            ((j + 1) / width) * screen_width, (i / height) * screen_height);
                                    g.draw(line);
                                }
                            if (j != (height - 1)) //&& !cell.walls[2])//not south
                                if (graph.getEdge(DCells[(int) i][(int) j], DCells[(int) i][(int) (j + 1)]) != null) {
                                    line.setLine((j / width) * screen_width, ((i + 1) / height) * screen_height,
                                            ((j + 1) / width) * screen_width, ((i + 1) / height) * screen_height);
                                    g.draw(line);
                                }
*
* */
