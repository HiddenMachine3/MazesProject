package Maze.Logic;

public class Cell {
    public int x, y;
    public boolean filled = false, visited;
    public boolean[] walls = new boolean[]{true, true, true, true};//n,e,s,w

    public Cell(boolean filled) {
        this.filled = filled;
    }

    public Cell(int x, int y, boolean filled) {
        this.x = x;
        this.y = y;
        this.filled = filled;
    }

    public void set(int x, int y, boolean filled) {
        this.x = x;
        this.y = y;
        this.filled = filled;
    }

    public void reset() {
        filled = false;
        for (int i = 0; i < 4; i++)
            walls[i] = true;
    }
}
