package com.shukebeta.zhong.mazegame;

import org.junit.jupiter.api.Test;

import java.security.InvalidParameterException;

import ara.bc282.assignment1.zhong.Colours;
import ara.bc282.assignment1.zhong.Directions;
import ara.bc282.assignment1.zhong.Eyeball;
import ara.bc282.assignment1.zhong.GameMap;
import ara.bc282.assignment1.zhong.Piece;
import ara.bc282.assignment1.zhong.PieceRole;
import ara.bc282.assignment1.zhong.Shapes;
import ara.bc282.assignment1.zhong.SoundEffect;
import ara.bc282.assignment1.zhong.Sprite;

import static org.junit.jupiter.api.Assertions.*;

class EyeballTest {

    @org.junit.jupiter.api.BeforeEach
    void setUp() {
        Sprite.setDelayTime(1);
        SoundEffect.setSoundOn(false);
    }

    /**
     1. !the game must have at least one stage
     2. !the game must have at least one goal
     3. !the game must have one and have only one start point
     4. !the game must indicate the current direction for the sprite
     5. !the game can display the current costing time for the current game
     6. !the game should show a warning sign on target square for illegal movement
     7. !the game should show a warning pop up for illegal movement
     8. !the game should congratulate the player if he or she wins
     9. !the game should have at least three sound effects: for a successful movement, for a win, and for an illegal movement
     10. !the game can display a total movement count
     11. !the game can show the solution
     12. !the game allows a player to choose one of the stages if the game has more than one stage (test case is same to feature 1)
     13. !the game allows a player to undo a move till he or she goes back to the start point
     14. !the game sound effect can be switched sound on / sound off
     15. !the game can be restarted at any time before the game end up
     16. !the symbol must be one of six predefined different colours
     17. !the symbol must be one of six predefined different shapes
     18. !the sprite cannot move backwards
     19. !the sprite cannot travel nocolour_blank squares
     20. !the sprite can and only can move forwards, move towards left and  move towards right with the same symbol
     21. !the sprite can and only can move forwards, move towards left and move towards right with the same colour
     */

    @Test //the game must have at least one stage
    void testAtLeastOneStage() {
        assertTrue(GameMap.getStageCount() >= 1);
    }

    @Test //the game must have at least one stage
    void testAtLeastOneGual() {
        GameMap g = new GameMap(1);
        assertTrue(g.getGoalList().size() >= 1);
    }

    @Test //the game must have one and have only one start point
    void testOnlyOneStartPoint() {
        for (int stage = 0; stage < GameMap.getStageCount(); stage++) {
            GameMap g = new GameMap(stage);
            assertEquals(1, g.getStartPointList().size());
        }
    }

    @Test //the game must indicate the current direction for the sprite
    void testIndicateCurrentDirectionAndCanDisplayCurrentCostTime() {
        GameMap m = new GameMap(1);
        Sprite s = new Sprite(m);
        // goto RED DIAMOND
        s.walkTo(4,1);
        // Directions no change
        assertSame(s.currentDirection, Directions.NORTH, "direction should be NORTH, but " + s.currentDirection + " found");
        s.walkTo(4,3);
        // newDirection should be EAST
        assertSame(s.currentDirection, Directions.EAST, "direction should be EAST, but " + s.currentDirection + " found");
        //testCanDisplayCurrentCostTime
        assertTrue(s.getCostTime() > 0);
    }

    //the game can display a total movement count,
    @Test  // the game allows a player to undo a move till he or she goes back to the start point
    void testCanDisplayTotalMoveAndUndo() {
        GameMap m = new GameMap(1);
        Sprite s = new Sprite(m);
        // goto RED DIAMOND
        assertTrue(s.walkTo(4,1));
        // Directions no change
        assertTrue(s.walkTo(4,3));
        assertEquals(2, s.getTotalMove());

        assertTrue(s.undoWalk());
        assertEquals(1, s.getTotalMove());
        assertTrue(s.currentPiece.x == 4 && s.currentPiece.y == 1);
        //undoWalk to start point
        assertTrue(s.undoWalk());
        //cannot undoWalk anymore
        assertFalse(s.undoWalk());
    }

    @Test //the game can be restarted at any time before the game end up
    void testCanRestart() {
        Eyeball gc = new Eyeball();
        gc.start(1);
        gc.sprite.walkTo(4, 1);
        assertEquals(1, gc.sprite.getTotalMove());
        gc.restart();
        assertEquals(0, gc.sprite.getTotalMove());
    }

    @Test
    void testWhenGameStartSpriteIsAtTheStartPoint() {
        Eyeball gc = new Eyeball();
        gc.start(0);

        //sprite is at start point
        assertTrue(gc.sprite.currentPiece.isStartPoint());
    }

    /*
        {new Piece(),                             new Piece(),                              new Piece(Colours.RED, Shapes.FLOWER, PieceRole.GOAL),   new Piece()},
        {new Piece(Colours.BLUE, Shapes.CROSS),   new Piece(Colours.YELLOW, Shapes.FLOWER), new Piece(Colours.YELLOW, Shapes.DIAMOND), new Piece(Colours.GREEN, Shapes.CROSS)},
        {new Piece(Colours.GREEN, Shapes.FLOWER), new Piece(Colours.RED, Shapes.STAR),      new Piece(Colours.GREEN, Shapes.STAR),     new Piece(Colours.YELLOW, Shapes.DIAMOND)},
        {new Piece(Colours.RED, Shapes.FLOWER),   new Piece(Colours.BLUE, Shapes.FLOWER),   new Piece(Colours.RED, Shapes.STAR),       new Piece(Colours.GREEN, Shapes.FLOWER)},
        {new Piece(Colours.BLUE, Shapes.STAR),    new Piece(Colours.RED, Shapes.DIAMOND),   new Piece(Colours.BLUE, Shapes.FLOWER),    new Piece(Colours.BLUE, Shapes.DIAMOND)},
        {new Piece(),                             new Piece(Colours.BLUE, Shapes.DIAMOND, PieceRole.START_POINT), new Piece(),         new Piece()}
     | -----------------> col/y 0-3
     | 0,0 0,1 0,2 0,3
     | 1,0 1,1 1,2 1,3
     | 2,0 2,1 2,2 2,3
     | 3,0 3,1 3,2 3,3
     | 4,0 4,1 4,2 4,3
     | 5,0 5,1 5,2 5,3
  row/x 0-5
     */    @Test
    void testCanMoveForwards() {

        Eyeball gc = new Eyeball();
        gc.start(1);

        // same column, same shape, same direction
        assertTrue(gc.sprite.walkTo(4, 1));
    }

    @Test
    void testCanMoveTowardsRight() {
        Eyeball gc = new Eyeball();
        gc.start(1);

        // same column, same shape, same direction
        assertTrue(gc.sprite.walkTo(4, 1));

        // same column, same shape, right direction
        assertTrue(gc.sprite.walkTo(4, 3));
    }

    @Test
    void testCanMoveTowardsLeft() {
        Eyeball gc = new Eyeball();
        gc.start(0);

        //solution for stage 0: {5,1},{3,1},{3,3},{1,3},{1,0},{4,0},{4,2},{0,2}

        // same column, same color(blue), same direction
        assertTrue(gc.sprite.walkTo(3, 1));

        // same row, same shape(flower), right direction
        assertTrue(gc.sprite.walkTo(3, 3));

        // same column, same color(green), left direction
        assertTrue(gc.sprite.walkTo(1,3));
    }

    @Test
    void testCanMoveSameColourAtSameColumn() {
        Eyeball gc = new Eyeball();
        gc.start(0);

        Sprite s = gc.sprite;
        //sprite is at start point：BLUE DIAMOND
        assertTrue(s.currentPiece.isStartPoint());


        // 3,1 is a piece of BLUE FLOWER
        Piece p = gc.currentMap.getPiece(3,1);
        // same colour
        assertSame(p.colour, s.currentPiece.colour);

        // and they are in same column
        assertEquals(s.currentPiece.y, p.y);

        // so the sprite can walk to 3, 1
        assertTrue(s.walkTo(3,1));
    }

    @Test
    void testCanMoveSameColourAtSameRow() {
        Eyeball gc = new Eyeball();
        gc.start(1);

        Sprite s = gc.sprite;
        //sprite is at start point：BLUE DIAMOND
        assertTrue(s.currentPiece.isStartPoint());

        //solution for stage 1: {5,1},{4,1},{2,1},{2,2},{3,2},{3,0},{2,0},{2,3},{3,3},{3,2},{0,2}
        assertTrue(s.walkTo(5,1));
        assertTrue(s.walkTo(4,1));
        assertTrue(s.walkTo(2,1));
        assertTrue(s.walkTo(2,2));

        // 3,2 is a piece of RED STAR
        assertTrue(s.walkTo(3,2));

        // 3,0 is a piece of RED FLOWER
        Piece p = gc.currentMap.getPiece(3,0);
        // same colour
        assertSame(p.colour, s.currentPiece.colour);

        // and they are in same row
        assertEquals(s.currentPiece.x, p.x);

        // so the sprite can walk to 3, 0
        assertTrue(s.walkTo(3,0));
    }

    @Test
    void testCanMoveSameShapeAtSameColumn() {
        Eyeball gc = new Eyeball();
        gc.start(0);

        Sprite s = gc.sprite;
        //sprite is at start point：BLUE DIAMOND
        assertTrue(s.currentPiece.isStartPoint());


        // 4,1 is a piece of RED DIAMOND
        Piece p = gc.currentMap.getPiece(4,1);
        // same shape
        assertSame(p.shape, s.currentPiece.shape);

        // and they are in same column
        assertEquals(s.currentPiece.y, p.y);

        // so the sprite can walk to 4, 1
        assertTrue(s.walkTo(4,1));
    }

    @Test
    void testCanMoveSameShapeAtSameRow() {
        Eyeball gc = new Eyeball();
        gc.start(0);

        Sprite s = gc.sprite;
        //sprite is at start point：BLUE DIAMOND
        assertTrue(s.currentPiece.isStartPoint());

        //soulution for stage1: {5,1},{3,1},{3,3},{1,3},{1,0},{4,0},{4,2},{0,2}
        assertTrue(s.walkTo(5,1));
        assertTrue(s.walkTo(3,1));
        assertTrue(s.walkTo(3,3));
        assertTrue(s.walkTo(1,3));
        // same row
        Piece p = gc.currentMap.getPiece(1,0);
        // and same shape
        assertSame(p.shape, s.currentPiece.shape);
        // so the sprite can walk to 1, 0
        assertTrue(s.walkTo(1,0));
    }

    @Test
    void testCanNotMoveSameColourAtDifferentColumnAndDifferentRow() {
        Eyeball gc = new Eyeball();
        gc.start(0);

        Sprite s = gc.sprite;
        //sprite is at start point：BLUE DIAMOND
        assertTrue(s.currentPiece.isStartPoint());


        // 4,2 is a piece of BLUE FLOWER
        Piece p = gc.currentMap.getPiece(4,2);
        // same colour
        assertSame(p.colour, s.currentPiece.colour);

        // but they are in different column and different row
        assertTrue(s.currentPiece.y != p.y);

        // so the sprite cannot walk to 4, 2
        assertFalse(s.walkTo(4,2));
    }

    @Test
    void testCanMoveSameShapeAtDifferentColumnAndDifferentRow() {
        Eyeball gc = new Eyeball();
        gc.start(0);

        Sprite s = gc.sprite;
        //sprite is at start point：BLUE DIAMOND
        assertTrue(s.currentPiece.isStartPoint());


        // 1,2 is a piece of YELLOW DIAMOND
        Piece p = gc.currentMap.getPiece(1,2);
        // same shape
        assertSame(p.shape, s.currentPiece.shape);

        // and they are in Different column and different row
        assertTrue(s.currentPiece.y != p.y);

        // so the sprite cannot walk to 1, 2
        assertFalse(s.walkTo(1,2));
    }

    @Test //the sprite cannot move backwards
    void testCannotMoveBackwards() {
        GameMap m = new GameMap(1);
        Sprite s = new Sprite(m);

        //sprite is at start point
        assertTrue(s.currentPiece.isStartPoint());

        assertSame(s.currentPiece.shape, Shapes.DIAMOND);
        // goto RED DIAMOND
        assertTrue(s.walkTo(4,1));
        assertSame(s.currentPiece.shape, Shapes.DIAMOND);
        // goto BLUE DIAMOND
        assertTrue(s.walkTo(4,3));
        assertSame(s.currentPiece.shape, Shapes.DIAMOND);

        // cannot walk back
        assertFalse(s.walkTo(4,1));
    }

    @Test //the sprite cannot travel nocolour_blank squares
    void testCannotTravelBlankPiece() {
        GameMap m = new GameMap(1);
        Sprite s = new Sprite(m);

        // 5,0 is a nocolour_blank piece
        assertTrue(m.getPiece(5, 0).isBlank());
        assertFalse(s.walkTo(5, 0));
    }

    @Test
    void testPieceMustBeOneOfPredefinedColors() {
        GameMap m = new GameMap(0);
        for(Piece[] row : m.getMapList()[0]) {
            for (Piece p: row) {
                assertTrue(p.colour instanceof Colours);
            }
        }
    }

    @Test
    void testPieceMustBeOneOfPredefinedShapes() {
        GameMap m = new GameMap(0);
        for(Piece[] row : m.getMapList()[0]) {
            for (Piece p: row) {
                assertTrue(p.shape instanceof Shapes);
            }
        }
    }

    @Test
    void testSoundEffectWalkSucceed() {
        SoundEffect.setSoundOn(true);
        SoundEffect.playSucceedMove();
        SoundEffect.setSoundOn(false);
    }

    @Test
    void testSoundEffectWarning() {
        SoundEffect.setSoundOn(true);
        SoundEffect.playWarning();
        SoundEffect.setSoundOn(false);
    }

    @Test
    void testSoundEffectCongratulations() {
        SoundEffect.setSoundOn(true);
        SoundEffect.playCongratulations();
        SoundEffect.setSoundOn(false);
        Sprite.delay(2000);
    }

    @Test
    void testCanShowSolutionStage0() {
        Eyeball gc = new Eyeball();
        SoundEffect.setSoundOn(false);
        assertTrue(gc.showSolution(0));
    }

    @Test
    void testCanShowSolutionStage1() {
        Eyeball gc = new Eyeball();
        SoundEffect.setSoundOn(false);
        assertTrue(gc.showSolution(1));
    }

    @Test
    void testIsTile() {
        GameMap m = new GameMap(1);
        Sprite s = new Sprite(m);

        // 0,0 is a nocolour_blank tile
        assertTrue(m.getPiece(0,0).isTile());
    }


    @Test
    void testIsGoal() {
        GameMap m = new GameMap(1);
        Sprite s = new Sprite(m);

        // 0,2 is the goal of stage 1
        assertTrue(m.getPiece(0,2).isGoal());
    }

    @Test
    void testIsStartPoint() {
        GameMap m = new GameMap(1);
        Sprite s = new Sprite(m);

        // 5,1 is the start point of stage 1
        assertTrue(m.getPiece(5,1).isStartPoint());
    }

    @Test
    void testCreateBlankPiece() {
        Piece p = new Piece();
        assertTrue(p.isBlank());
    }

    @Test
    void testCreateStartPoint() {
        Piece p = new Piece(Colours.BLUE, Shapes.DIAMOND, PieceRole.START_POINT);
        assertTrue(p.isStartPoint());
    }

    @Test
    void testCreateGoal() {
        Piece p = new Piece(Colours.BLUE, Shapes.DIAMOND, PieceRole.GOAL);
        assertTrue(p.isGoal());
    }

    @Test
    void testCreateTile() {
        Piece p = new Piece(Colours.BLUE, Shapes.DIAMOND);
        assertTrue(p.isTile());
    }

    @Test
    void testGetPiece() {
        GameMap map = new GameMap(1);
        assertTrue(map.getPiece(0,0).isBlank());
    }

    @Test
    void testGetInvalidPiece() {
        GameMap map = new GameMap(1);
        assertThrows(InvalidParameterException.class, () -> {map.getPiece(10,10);});
    }

    @Test
    void testIsSoundOn() {
        SoundEffect.setSoundOn(true);
        assertTrue(SoundEffect.isSoundOn());
    }

    @Test
    void testIsSoundOff() {
        SoundEffect.setSoundOn(false);
        assertFalse(SoundEffect.isSoundOn());
    }

    @Test
    void testCreateSprite() {
        Sprite sprite = new Sprite(new GameMap(0));
        assertTrue(sprite.currentPiece.isStartPoint());
    }

    @Test
    void testSetDelayTime() {
        Sprite.setDelayTime(300);
        assertEquals(Sprite.getDelayTime(), 300);
        Sprite.delay(1);
    }

    @Test
    void testSetDirection() {
        Sprite sprite = new Sprite(new GameMap(0));
        sprite.setDirection(Directions.WEST);
        assertSame(sprite.currentDirection, Directions.WEST);
    }

    @Test
    void testPieceToString() {
        GameMap m = new GameMap(1);
        Piece startPoint = m.getStartPoint();
        assertEquals(startPoint.toString(), "Piece at x: 5 y: 1\ncolour: BLUE shape: DIAMOND\nrole: START_POINT");
    }

    @Test
    void testGetInvalidMap() {
        assertThrows(InvalidParameterException.class, () -> {(new GameMap(1)).getMap(3);});
    }
}
