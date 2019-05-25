package ara.bc282.assignment1.zhong;

public class Piece {
    PieceRole role;
    public Shapes shape;
    public Colours colour;
    private Directions spriteDirection;
    public int x, y;

    public Piece() {
        role = PieceRole.TILE;
        shape = Shapes.BLANK;
        colour = Colours.NOCOLOUR;
        setPos(0, 0);
        setSpriteDirection(Directions.UNDEFINED);
    }

    public Piece(Colours colour, Shapes shape) {
        this();
        this.shape = shape;
        this.colour = colour;
    }

    public Piece( Colours colour, Shapes shape, PieceRole role) {
        this(colour, shape);
        this.role = role;
    }

    public void setPos(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isGoal() {
        return role == PieceRole.GOAL;
    }

    public boolean isStartPoint() {
        return role == PieceRole.START_POINT;
    }

    public boolean isTile() {
        return role == PieceRole.TILE;
    }

    public boolean isBlank() {
        return shape == Shapes.BLANK;
    }

    public Directions getSpriteDirection() {
        return spriteDirection;
    }

    public void setSpriteDirection(Directions spriteDirection) {
        this.spriteDirection = spriteDirection;
    }

    public String toString() {
        return "Piece at x: " + x + " y: " + y + "\n" + "colour: " + colour + " shape: " + shape + "\n" + "role: " + role;
    }
}
