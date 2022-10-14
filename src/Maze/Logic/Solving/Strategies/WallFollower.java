package Maze.Logic.Solving.Strategies;

import Maze.Logic.Solving.SolvingStrategy;

import java.awt.*;

/**
 * ____
 * | ^ |
 * | | |
 * [It's just solving the maze by "hugging" the wall, i.e., following either the right or left wall until you reach the end
 * Although, this only works if the maze consists of a continuous wall,i.e, if all the walls are connected]
 * <p>
 * loop:
 * case1 - forwardWall,rightWall,leftWall
 * *               >turn around(U-turn)
 * >moveForward
 * case2 - NO_RightWall?
 * *               >turn right
 * >moveForward
 * case3 - NO_ForwardWall?
 * *               >moveForward
 * case3 - NO_LeftWall?
 * *               >turnLeft
 * >moveForward
 */


public class WallFollower extends SolvingStrategy {


    int forwardDir, perpendicularDir;
    Point[] Points = {
            new Point(0, -1),
            new Point(1, 0),  //0,1,2,3
            new Point(0, 1),
            new Point(-1, 0)};//n,e,s,w
    /*
              /0N\
           3W______E1
              \S2/
     */

    @Override
    public void initialize() {
        forwardDir = 1;
        perpendicularDir = getNextDir(forwardDir);
        cx = sx;
        cy = sy;
        Grid[cx][cy].filled = true;//filling the starting cell
    }

    /**
     * I have no idea what this code does. It was written in 2020 :/
     */
    @Override
    public void solve() {
        while (!((cx == ex) && (cy == ey))) {
            boolean isWallForward = getforwardWall(), isPerpWall = getPerpWall(), isAntiPerpWall = getAntiPerpWall();
            if (isWallForward && isPerpWall && isAntiPerpWall) {//  |```|
                turn();//U turn
                turn();
                moveForward();
            } else if (!isPerpWall) {//   |```  or  ``` or '   '
                turn();
                moveForward();
            } else if (!isWallForward) {// |  |  or '   |'
                moveForward();
            } else if (!isAntiPerpWall) {//  '''|
                turnAnti();
                moveForward();
            }

            Grid[cx][cy].filled = true;
            updateImage();
        }
        System.out.println("Finished Solving");
    }

    void moveForward() {
        Point Point = getPoint(forwardDir);
        cx += Point.x;
        cy += Point.y;
    }

    boolean getforwardWall() {
        return getWallInDir(cx, cy, forwardDir);
    }

    boolean getPerpWall() {
        return getWallInDir(cx, cy, perpendicularDir);
    }

    boolean getAntiPerpWall() {
        return getWallInDir(cx, cy, getNextDir(getNextDir(perpendicularDir)));
    }

    void turn() {
        forwardDir = getNextDir(forwardDir);
        perpendicularDir = getNextDir(forwardDir);
    }

    void turnAnti() {
        turn();
        turn();
        turn();
    }

    /*
    *         /0N\
           3W______E2
              \S1/
    * */

    /*int getPrevDir(int testDir){
        int prev = getNextDir (getNextDir (getNextDir (testDir)));
        return prev;
    }*/

    int getNextDir(int testDir) {
        return (testDir + 1) % 4;
    }

    Point getPoint(int i) {
        return Points[i];
    }

    boolean getWallInDir(int x, int y, int dir) {
        return Grid[x][y].walls[dir];
    }

}
