package MoveParameters;

public enum MapDirection {
    NORTH(0), NORTHWEST(1), NORTHEAST(2), SOUTH(3), SOUTHWEST(4), SOUTHEAST(5), WEST(6), EAST(7);

    public int value;

    MapDirection(int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }

    public MapDirection next() {
        switch (this) {
            case NORTH: return NORTHEAST;
            case NORTHEAST: return EAST;
            case EAST: return SOUTHEAST;
            case SOUTHEAST: return SOUTH;
            case SOUTH: return SOUTHWEST;
            case SOUTHWEST: return WEST;
            case WEST: return NORTHWEST;
            case NORTHWEST: return NORTH;
        }
        throw new IllegalStateException("Wrong direction.");
    }

    public Vector2d toUnitVector() {
        switch (this) {
            case EAST: return  new Vector2d(1, 0);
            case WEST: return  new Vector2d(-1, 0);
            case NORTH: return  new Vector2d(0, 1);
            case NORTHEAST: return new Vector2d(1,1);
            case NORTHWEST: return new Vector2d(-1,1);
            case SOUTH: return  new Vector2d(0, -1);
            case SOUTHEAST: return new Vector2d(1,-1);
            case SOUTHWEST: return new Vector2d(-1,-1);
        }
        throw new IllegalStateException("Wrong direction.");
    }
}
