package ara.bc282.assignment1.zhong;

import android.util.Log;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

public class Sprite {
    public GameMap map;
    public Piece currentPiece;
    public Directions currentDirection;

    private Timestamp startTime;
    private ArrayList<Piece> walkedPieceList;

    private static int delayTime = 200;

    public Sprite(GameMap aMap) {
        map = aMap;
        currentDirection = Directions.NORTH;
        currentPiece = map.getStartPoint();
        currentPiece.setSpriteDirection(currentDirection);

        startTime = new Timestamp((new Date()).getTime());
        walkedPieceList = new ArrayList<>();
        walkedPieceList.add(currentPiece);
    }

    public static int getDelayTime() {
        return delayTime;
    }

    public static void setDelayTime(int newDelayTime) {
        delayTime = newDelayTime;
    }

    public void setDirection(Directions newDirection) {
        currentDirection = newDirection;
    }

    public boolean canMove(int x, int y) {

        /**
          | -----------------> col/y 0-3
          | 0,0 0,1 0,2 0,3
          | 1,0 1,1 1,2 1,3
          | 2,0 2,1 2,2 2,3
          | 3,0 3,1 3,2 3,3
          | 4,0 4,1 4,2 4,3
          | 5,0 5,1 5,2 5,3
         row/x 0-5
         */
        boolean allowMove = false;
        Piece p = map.getPiece(x, y);
        Directions nextDirection = currentDirection;
        if (p.x == currentPiece.x || p.y == currentPiece.y) {
            if (p.x == currentPiece.x) {
                nextDirection = (y > currentPiece.y ? Directions.EAST : Directions.WEST);
            } else if(p.y == currentPiece.y) {
                nextDirection = (x > currentPiece.x ? Directions.SOUTH : Directions.NORTH);
            }
            switch (currentDirection) {
                case NORTH:
                    allowMove = nextDirection != Directions.SOUTH;
                    break;
                case SOUTH:
                    allowMove = nextDirection != Directions.NORTH;
                    break;
                case EAST:
                    allowMove = nextDirection != Directions.WEST;
                    break;
                case WEST:
                    allowMove = nextDirection != Directions.EAST;
            }
            Log.d(currentDirection.toString(), "|" + nextDirection.toString());
        } else {
            allowMove = false;
        }
        return allowMove && (p.colour == currentPiece.colour || p.shape == currentPiece.shape);
    }

    public Directions getNextDirection(int x, int y) {
        Directions nextDirection = currentDirection;
        if (currentPiece.x == x && currentPiece.y == y) {
            nextDirection = currentDirection;
        } else if (x > currentPiece.x) {
            nextDirection = Directions.SOUTH;
        } else if (x < currentPiece.x) {
            nextDirection = Directions.NORTH;
        } else if (y > currentPiece.y) {
            nextDirection = Directions.EAST;
        } else if (y < currentPiece.y) {
            nextDirection = Directions.WEST;
        }
        return nextDirection;
    }

    // this method don't check if the sprite can move to x, y
    public void gotoPiece(int x, int y) {
        delay(delayTime);
        currentDirection = getNextDirection(x,y);
        currentPiece = map.getPiece(x, y);
        currentPiece.setSpriteDirection(currentDirection);
        walkedPieceList.add(currentPiece);
    }

    public static void delay(int millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    //// this method check if the sprite can move to x, y first, then call gotoPiece
    public boolean walkTo(int x, int y) {
        if(canMove(x, y)) {
            gotoPiece(x, y);
            if (currentPiece.isGoal()) {
                SoundEffect.playCongratulations();
            } else {
                SoundEffect.playSucceedMove();
            }
            return true;
        } else {
            System.out.println("\t" + x + ":" + y);
            map.drawWarning(x, y, delayTime);
            SoundEffect.playWarning();
        }
        return false;
    }

    public boolean canUndo() {
        return walkedPieceList.size() > 1;
    }

    // undoWalk is walkBack
    public boolean undoWalk() {
        if (canUndo()) {
            delay(delayTime);
            // delete current piece
            walkedPieceList.remove(walkedPieceList.get(walkedPieceList.size() - 1));

            // get true last piece
            currentPiece = walkedPieceList.get(walkedPieceList.size() - 1);
            currentDirection = currentPiece.getSpriteDirection();
            return true;
        }
        System.out.println("You have back to the start point, cannot undoWalk any more!");
        return false;
    }

    public int getTotalMove() {
        return walkedPieceList.size() - 1; // exclude the piece at start_point
    }

    public long getCostTime() {
        return (new Date().getTime() - startTime.getTime());
    }

}
