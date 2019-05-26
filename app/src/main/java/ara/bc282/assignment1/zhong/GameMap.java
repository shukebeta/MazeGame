package ara.bc282.assignment1.zhong;

import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Map;

public class GameMap {
    public Piece[][] map;

    private static final Piece[][][] mapList;

    static {
        mapList = new Piece[][][]{
                {
                        {new Piece(), new Piece(), new Piece(Colours.RED, Shapes.FLOWER, PieceRole.GOAL), new Piece()},
                        {new Piece(Colours.BLUE, Shapes.CROSS), new Piece(Colours.YELLOW, Shapes.FLOWER), new Piece(Colours.YELLOW, Shapes.DIAMOND), new Piece(Colours.GREEN, Shapes.CROSS)},
                        {new Piece(Colours.GREEN, Shapes.FLOWER), new Piece(Colours.RED, Shapes.STAR), new Piece(Colours.GREEN, Shapes.STAR), new Piece(Colours.YELLOW, Shapes.DIAMOND)},
                        {new Piece(Colours.RED, Shapes.FLOWER), new Piece(Colours.BLUE, Shapes.FLOWER), new Piece(Colours.RED, Shapes.STAR), new Piece(Colours.GREEN, Shapes.FLOWER)},
                        {new Piece(Colours.BLUE, Shapes.STAR), new Piece(Colours.RED, Shapes.DIAMOND), new Piece(Colours.BLUE, Shapes.FLOWER), new Piece(Colours.BLUE, Shapes.DIAMOND)},
                        {new Piece(), new Piece(Colours.BLUE, Shapes.DIAMOND, PieceRole.START_POINT), new Piece(), new Piece()}
                },
                {
                        {new Piece(), new Piece(), new Piece(Colours.RED, Shapes.FLOWER, PieceRole.GOAL), new Piece()},
                        {new Piece(Colours.BLUE, Shapes.CROSS), new Piece(Colours.BLUE, Shapes.FLOWER), new Piece(Colours.BLUE, Shapes.DIAMOND), new Piece(Colours.GREEN, Shapes.CROSS)},
                        {new Piece(Colours.GREEN, Shapes.FLOWER), new Piece(Colours.RED, Shapes.STAR), new Piece(Colours.GREEN, Shapes.STAR), new Piece(Colours.YELLOW, Shapes.FLOWER)},
                        {new Piece(Colours.RED, Shapes.FLOWER), new Piece(Colours.GREEN, Shapes.DIAMOND), new Piece(Colours.RED, Shapes.STAR), new Piece(Colours.YELLOW, Shapes.STAR)},
                        {new Piece(Colours.GREEN, Shapes.CROSS), new Piece(Colours.RED, Shapes.DIAMOND), new Piece(Colours.BLUE, Shapes.FLOWER), new Piece(Colours.GREEN, Shapes.DIAMOND)},
                        {new Piece(), new Piece(Colours.BLUE, Shapes.DIAMOND, PieceRole.START_POINT), new Piece(), new Piece()}
                }
        };
    }

    public static final int[][][] solutionList = {
            {
                    {5,1},{3,1},{3,3},{1,3},{1,0},{4,0},{4,2},{0,2}
            },
            {
                    {5,1},{4,1},{2,1},{2,2},{3,2},{3,0},{2,0},{2,3},{3,3},{3,2},{0,2}
            }
    };

    public GameMap(int stage) {
        map = getMap(stage);
        for(int x = 0; x < map.length; x++) {
            for(int y = 0; y < map[x].length; y++) {
                map[x][y].setPos(x,y);
            }
        }
    }

    public static int getStageCount() {
        return mapList.length;
    }

    public static Piece[][][] getMapList() {
        return mapList;
    }

    public ArrayList<Piece> getGoalList() {
        ArrayList<Piece> goalList = new ArrayList<>();
        for(int x = 0; x < map.length; x++) {
            for(int y = 0; y < map[x].length; y++) {
                if(map[x][y].isGoal()) {
                    goalList.add(map[x][y]);
                }
            }
        }
        return goalList;
    }

    public ArrayList<Piece> getStartPointList() {
        ArrayList<Piece> startPointList = new ArrayList<>();
        for(int x = 0; x < map.length; x++) {
            for(int y = 0; y < map[x].length; y++) {
                if(map[x][y].isStartPoint()) {
                    startPointList.add(map[x][y]);
                }
            }
        }
        return startPointList;
    }


    public Piece getPiece(int x, int y) {
        if (x <= map.length && y <= map[0].length) {
            return map[x][y];
        } else {
            throw new InvalidParameterException("x = " + x + " y = " + y + " is invalid!");
        }
    }

    public Piece getStartPoint() {
        ArrayList<Piece> startPointList = getStartPointList();
        return startPointList.get(0);
    }

    public Piece[][] getMap(int stageNo) {
        if (stageNo < 0 || stageNo > 1) {
            throw new InvalidParameterException("Invalid stage no:" + stageNo);
        }
        return mapList[stageNo];
    }

    public void drawWarning(int x, int y, int durationInMillis) {
        System.out.println("TILE at x:" + x + " y:" + y + " now have A WARNING X symbol covered on it");
        System.out.println("TILE at x:" + x + " y:" + y + " now have A WARNING window poped up");
        Sprite.delay(durationInMillis);
        System.out.println("TILE at x:" + x + " y:" + y + " now have NO WARNING X symbol covered on it");
        System.out.println("TILE at x:" + x + " y:" + y + " now have NO WARNING window poped up");
    }


}
