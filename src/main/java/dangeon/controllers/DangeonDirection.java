package dangeon.controllers;


public enum DangeonDirection {
    NORTH(0, 1),
    EAST(1, 0),
    SOUTH(0, -1),
    WEST(-1, 0),
    NORTH_EAST(1, 1),
    SOUTH_EAST(1, -1),
    SOUTH_WEST(-1, -1),
    NORTH_WEST(-1, 1),
    NONE(0, 0);

    private final int dx;
    private final int dy;

    DangeonDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    public float getAngle() {
        return 0;
    }

    public DangeonDirection combine(DangeonDirection other) {

        int newDx = this.dx + other.dx;
        int newDy = this.dy + other.dy;
        for (DangeonDirection dir : DangeonDirection.values()) {
            if (dir.dx == newDx && dir.dy == newDy) {
                return dir;
            }
        }
        return NONE;
    }
}
