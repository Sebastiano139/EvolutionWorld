package World;

import MoveParameters.Vector2d;

import java.util.ArrayList;

public class Jungle {
    private Vector2d lowerLeft;
    private Vector2d upperRight;
    private ArrayList<Vector2d> freePostion = new ArrayList<Vector2d>();
    private WorldMap map;

    public String toString() {
        return this.lowerLeft + " " + this.upperRight;
    }

    Jungle(Vector2d upperRight, Vector2d lowerLeft, WorldMap map) {
        this.upperRight = upperRight;
        this.lowerLeft = lowerLeft;
        this.map = map;
        for(int i = this.lowerLeft.x; i <= this.upperRight.x; i++)
            for(int j = this.lowerLeft.y; j <= this.upperRight.y; j++) {
                this.freePostion.add(new Vector2d(i,j));
            }

    }


    public Vector2d getLowerLeft() { return this.lowerLeft; }
    public Vector2d getUpperRight() { return this.upperRight; }
}
