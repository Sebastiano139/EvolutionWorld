package MapElements;

import MoveParameters.MapDirection;
import MoveParameters.Vector2d;
import World.Jungle;
import World.WorldMap;

import java.awt.*;
import java.util.Random;

import static MoveParameters.MapDirection.*;

public class Animal implements IMapElement {
    private MapDirection orientation;
    private Vector2d position = new Vector2d(2, 2);
    private Genes genes;
    private int energy;
    private WorldMap map;

    public Animal( WorldMap map ) {
        this.map = map;
        this.orientation = NORTH;
        for (int i = 0; i<new Random().nextInt(8); i++) {
            this.orientation = this.orientation.next();
        }
    }

    public Animal(Animal parentA, Animal parentB, WorldMap map) {
        this(map);
        this.energy = parentA.getEnergy()/4 + parentB.getEnergy()/4;
        this.genes = new Genes(parentA.getGenes(), parentB.getGenes());
        this.position = parentA.getPosition();
    }

    public Animal(Vector2d initialPosition, int energy, WorldMap map) {
        this(map);
        this.energy = energy;
        this.position = initialPosition;
        this.genes = new Genes();
    }

    public void reproduction() {
        this.energy -= this.energy/4;
    }

    public String toString() {
        switch (this.orientation) {
            case EAST: return "E ";
            case WEST: return "W ";
            case NORTHEAST: return "NE ";
            case NORTHWEST: return "NW ";
            case NORTH: return "N ";
            case SOUTHEAST: return "SE ";
            case SOUTHWEST: return "SW ";
            case SOUTH: return "S ";
        }
        throw new IllegalStateException("Wrong orientation on map.");
    }

    public void move() {
        this.orientation = moveOrientation();
        Vector2d oldPos = this.position;
        Vector2d newPos = this.position.add(this.orientation.toUnitVector(), this.map.getWidth(), this.map.getHeight());
        if( !(oldPos.equals(newPos)) && this.map.positionChanged(oldPos, newPos, this)) {
            this.position = newPos;
        }
    }

    public boolean isJungle(Jungle jungle) {
        return (this.position.follows(jungle.getLowerLeft()) && this.position.precedes(jungle.getUpperRight()));
    }

    public MapDirection moveOrientation() {
        int moveDirection = this.genes.getDirection();
        MapDirection tmpMapDirection = this.orientation;
        for (int i = 0; i<moveDirection; i++) {
            tmpMapDirection = tmpMapDirection.next();
        }
        return tmpMapDirection;
    }



    public void subtractEnergy(int MOVE_ENERGY) { this.energy -= MOVE_ENERGY; }
    public void feedAnimal(int PLANT_ENERGY) { this.energy += PLANT_ENERGY ; }
    public int getEnergy() { return this.energy; }
    public Genes getGenes() { return this.genes; }
    public MapDirection getOrientation() {
        return this.orientation;
    }
    public Vector2d getPosition() {
        return this.position;
    }



}
