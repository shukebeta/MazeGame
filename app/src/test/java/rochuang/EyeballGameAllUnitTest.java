package rochuang;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;

import ara.bc282.assignment1.rochuang.Direction;
import ara.bc282.assignment1.rochuang.Eyeball;
import ara.bc282.assignment1.rochuang.EyeballGame;
import ara.bc282.assignment1.rochuang.GameGridIron;
import ara.bc282.assignment1.rochuang.Piece;
import ara.bc282.assignment1.rochuang.PieceCharacter;

class EyeballGameAllUnitTest {

	void setup() {		
	}
///////////////////*EyeBall Class*///////////////////		
	@DisplayName("Test EyeyBall's total spending time is 0 at the beginning")
	@Test
	void testEyeballCreationTimeSpending() {
		double timeSpending = -1;
		Eyeball eb = new Eyeball (new GameGridIron(1));
		timeSpending = eb.getTotalMoveSpendingTime();
		assertTrue(timeSpending == 0);
	}	
	
	
	@DisplayName("Test EyeBall is StartPoint at the beginning")
	@Test
	void testEyeballStartPoint() {
		Eyeball eb = new Eyeball (new GameGridIron (1));
		assertTrue(eb.getMyCurrentPiece().isStartPoint());
	}
	
	@DisplayName("Test EyeBall is not Endpoint at the beginning")
	@Test
	void testEyeballEndPoint() {
		Eyeball eb = new Eyeball (new GameGridIron (1));
		assertTrue(! eb.getMyCurrentPiece().isEndPoint());
	}
	
	
	@DisplayName("Test EyeBall's total move time at the beginning")
	@Test
	void testEyeballCreation2() {
		Eyeball eb = new Eyeball (new GameGridIron (1));
		assertTrue(eb.getTotalMoveSpendingTime() == 0);
	}	
	
	@DisplayName("Test EyeyBall's total move at the beginning")
	@Test
	void testEyeballCreationTotalMove() {
		Eyeball eb = new Eyeball (new GameGridIron (1));
		int totalMove = 0;
		totalMove = eb.countTotalMove();
		assertTrue(totalMove == 0);
	}
	
	@DisplayName("Test EyeyBall's always facing North at the beginning in level 1")
	@Test
	void testEyeballCreationDirection() {
		Piece thePiece;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		thePiece = eb.getMyCurrentPiece();
		assertTrue(thePiece.myDirection == Direction.NORTH);
	}
	
	@DisplayName("Test EyeyBall's x position at the beginning in level 1")
	@Test
	void testEyeballCreationXPos() {
		Piece thePiece;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		thePiece = eb.getMyCurrentPiece();
		assertTrue(thePiece.x == 5);
	}
	
	@DisplayName("Test EyeyBall's y position at the beginning in level 1")
	@Test
	void testEyeballCreationYPos() {
		Piece thePiece;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		thePiece = eb.getMyCurrentPiece();
		assertTrue(thePiece.y == 1);
	}
	
	@DisplayName("Test EyeyBall's direction at the beginning in level 1")
	@Test
	void testEyeballFirstMoveDirection() {
		Piece thePiece;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		thePiece = eb.getMyCurrentPiece();
		assertTrue(thePiece.myDirection == Direction.NORTH);
	}
	
	@DisplayName("Test EyeyBall's can do first move for different color and different sharp")
	@Test
	void testEyeballCanDoFirstMove() {
		boolean isCanMove;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		isCanMove = eb.canDoNextMove(1,1);		
		assertTrue(isCanMove== false);
	}
	
	@DisplayName("Test EyeyBall's can do first move for same sharp")
	@Test
	void testEyeballCanDoFirstMoveForSameShape() {
		boolean isCanMove;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		isCanMove = eb.canDoNextMove(4,1);		
		assertTrue(isCanMove== true);
	}
	
	@DisplayName("Test EyeyBall's can do first move for same color")
	@Test
	void testEyeballCanDoFirstMoveForSameColor() {
		boolean isCanMove;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		isCanMove = eb.canDoNextMove(3,1);
		assertTrue(isCanMove== true);
	}
	
	@DisplayName("Test EyeyBall's can do first move for same sharp still face NORTH in level 1")
	@Test
	void testEyeballMoveSameSharpPieceFaceNorth() {
		Piece thePiece;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		eb.moveToNextPieceSucceed(4, 1);
		thePiece = eb.getMyCurrentPiece();
		assertTrue(eb.getMyCurrentDirection() ==Direction.NORTH);
	}
	
	@DisplayName("Test EyeyBall's can do first move for same color still face NORTH in level 1")
	@Test
	void testEyeballMoveSameColorPieceFaceNorth() {
		Piece thePiece;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		eb.moveToNextPieceSucceed(3, 1);
		thePiece = eb.getMyCurrentPiece();
		assertTrue(eb.getMyCurrentDirection() ==Direction.NORTH);
	}
	@DisplayName("Test EyeyBall's can do first move for same sharp is not yet final point level 1")
	@Test
	void testEyeballMoveSameSharpReachFinal() {
		Piece thePiece;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		eb.moveToNextPieceSucceed(4, 1);
		thePiece = eb.getMyCurrentPiece();
		assertTrue(eb.isReachingEndPoint() == false);
	}
	
	@DisplayName("Test EyeyBall's can do first move for same color is not yet final point level 1")
	@Test
	void testEyeballMoveSameColorReachFinal() {
		Piece thePiece;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		eb.moveToNextPieceSucceed(3, 1);
		thePiece = eb.getMyCurrentPiece();
		assertTrue(eb.isReachingEndPoint() == false);
	}
	
	
	@DisplayName("Test EyeyBall's can do first move for same color is not longer start point in level 1")
	@Test
	void testEyeballMoveSameColorAwayStartPoint() {
		Piece thePiece;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		eb.moveToNextPieceSucceed(3, 1);
		thePiece = eb.getMyCurrentPiece();
		assertTrue(eb.isReachingStartPoint() == false);
	}
	
	@DisplayName("Test EyeyBall's can do first move for same sharp is not longer start point in level 1")
	@Test
	void testEyeballMoveSameSharpAwayStartPoint() {
		Piece thePiece;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		eb.moveToNextPieceSucceed(4, 1);
		thePiece = eb.getMyCurrentPiece();
		assertTrue(eb.isReachingStartPoint()  == false);
	}
	
	@DisplayName("Test EyeyBall's total spending time is not 0 after moving in level 1")
	@Test
	void testEyeballMoveTimeSpending() {
		double timeSpending = 0;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		eb.moveToNextPieceSucceed(4, 1);
		eb.moveToNextPieceSucceed(4, 3);
		timeSpending = eb.getTotalMoveSpendingTime();
		assertTrue(timeSpending == 0);
	}	
	
	@DisplayName("Test EyeyBall's can move forward")
	@Test
	void testEyeballMoveForward() {
		double timeSpending = 0;
		Eyeball eb = new Eyeball (new GameGridIron (0));
		assertTrue(eb.moveToNextPieceSucceed(4, 1) == true);
	}
	
	@DisplayName("Test EyeyBall's cannot move backforward in level 2")
	@Test
	void testEyeballMoveBackward() {
		Eyeball eb = new Eyeball (new GameGridIron (1));
		eb.moveToNextPieceSucceed(3, 1);		
		assertTrue(eb.moveToNextPieceSucceed(2, 1) == false);
	}
	
	@DisplayName("Test EyeyBall's done 2 move steps in level 1")
	@Test
	void testEyeballMoveSteps() {
		Eyeball eb = new Eyeball (new GameGridIron (0));
		eb.moveToNextPieceSucceed(3, 1);
		eb.moveToNextPieceSucceed(1, 1);
		assertTrue(eb.getMyAllMovedPieces().size() ==3 );
	}
	
	@DisplayName("Test EyeyBall's facing east in level 1")
	@Test
	void testEyeballMovingEast() {
		Eyeball eb = new Eyeball (new GameGridIron (0));
		eb.moveToNextPieceSucceed(4, 1);
		eb.moveToNextPieceSucceed(4, 3);
		assertTrue(eb.getMyCurrentDirection() == Direction.EAST );
	}
	@DisplayName("Test EyeyBall's facing west in level 1")
	@Test
	void testEyeballMovingWest() {
		Eyeball eb = new Eyeball (new GameGridIron (0));
		eb.moveToNextPieceSucceed(3, 1);
		eb.moveToNextPieceSucceed(3, 0);
		assertTrue(eb.getMyCurrentDirection() == Direction.WEST);
	}
///////////////////*Piece Class*///////////////////
	/*
	 * @DisplayName
	 * 
	 * @Test void test1() { GameGridIron gr = new GameGridIron(0); }
	 */
	@DisplayName("Test Game StartPoint Piece")
	@Test
	void testGameStartPoint() {
		Piece thePiece;
		GameGridIron gr = new GameGridIron(0);
		thePiece = gr.findStartPointPiece();
		assertTrue(thePiece.myCharacter == PieceCharacter.START_POINT);
	}
	
	@DisplayName("Test Game EndPoint Piece")
	@Test
	void testGameEndPoint() {
		Piece thePiece;
		GameGridIron gr = new GameGridIron(0);
		thePiece = gr.findEndPointPiece();
		assertTrue(thePiece.myCharacter == PieceCharacter.END_POINT);
	}
	
	@DisplayName("Test Game Blank Piece")
	@Test
	void testGameBlankPiece() {
		Piece thePiece;
		GameGridIron gr = new GameGridIron(0);
		thePiece = gr.findPieceInMap(5, 0);
		assertTrue(thePiece.isBlank() == true);
	}
///////////////////*GameGridIron Class*///////////////////	
	@DisplayName("Test Game has 2 levels")
	@Test
	void test2GameLevels() {
		assertTrue(GameGridIron.countingMapLevel() >=2 );
	}
	
	@DisplayName("Test Game has 2 Solutions")
	@Test
	void test2GameSolutions() {
		assertTrue(GameGridIron.countingSolutions() >=2 );
	}
	
	@DisplayName("Test Game has Start Point in level 1")
	@Test
	void testGameHasStartPointLevelOne() {
		GameGridIron gr = new GameGridIron(0);
		Piece theStartPiece;		
		theStartPiece = gr.findStartPointPiece();
		assertTrue(theStartPiece.isStartPoint() == true);
	}
	
	@DisplayName("Test Game has End Point in level 1")
	@Test
	void testGameHasEndPointLevelOne() {
		GameGridIron gr = new GameGridIron(0);
		Piece theEndPiece;		
		theEndPiece = gr.findEndPointPiece();
		assertTrue(theEndPiece.isEndPoint() == true);
	}
	
	@DisplayName("Test Game has Start Point in level 2")
	@Test
	void testGameHasStartPointLevelTwo() {
		GameGridIron gr = new GameGridIron(1);
		Piece theStartPiece;		
		theStartPiece = gr.findStartPointPiece();
		assertTrue(theStartPiece.isStartPoint() == true);
	}
	
	@DisplayName("Test Game has End Point in level 2")
	@Test
	void testGameHasEndPointLevelTwo() {
		GameGridIron gr = new GameGridIron(1);
		Piece theEndPiece;		
		theEndPiece = gr.findEndPointPiece();
		assertTrue(theEndPiece.isEndPoint() == true);
	}
	
	@DisplayName("Test Game can find Gird Piece in level 1")
	@Test
	void testGameFindGirdPieceLevelOne() {
		GameGridIron gr = new GameGridIron(0);
		Piece thePiece;		
		thePiece = gr.findPieceInMap(2,0);
		assertTrue(thePiece.myCharacter == PieceCharacter.GRID);
	}
	
	@DisplayName("Test Game can find Blank Piece in level 1")
	@Test
	void testGameFindBlankPieceLevelOne() {
		GameGridIron gr = new GameGridIron(0);
		Piece thePiece;		
		thePiece = gr.findPieceInMap(5,0);
		assertTrue(thePiece.isBlank()==true);
	}
	
	@DisplayName("Test Game can find Gird Piece in level 2")
	@Test
	void testGameFindGridPieceLevelTwo() {
		GameGridIron gr = new GameGridIron(1);
		Piece thePiece;		
		thePiece = gr.findPieceInMap(3,0);
		assertTrue(thePiece.myCharacter == PieceCharacter.GRID);
	}
	
	@DisplayName("Test Game can find Blank Piece in level 2")
	@Test
	void testGameFindBlankPieceLevelTwo() {
		GameGridIron gr = new GameGridIron(1);
		Piece thePiece;		
		thePiece = gr.findPieceInMap(0,0);
		assertTrue(thePiece.isBlank()==true);
	}
///////////////////*EyeballGame Class*///////////////////	
	@DisplayName("Test Game can start")
	@Test
	void testEyegallGameStart() {
		Eyeball eb;
		EyeballGame ebGame = new EyeballGame();
		ebGame.startGame(1);
		eb = ebGame.eb;
		assertTrue(eb.getMyCurrentPiece().isStartPoint()==true);
	}
	
	@DisplayName("Test Game can restart")
	@Test
	void testEyegallGameRestart() {
		Eyeball eb;
		EyeballGame ebGame = new EyeballGame();
		ebGame.reStartGame(1);
		eb = ebGame.eb;
		assertTrue(eb.getMyCurrentPiece().isStartPoint()==true);
	}
	@DisplayName("Test Game can do solution in level 1 ")
	@Test
	void testEyegallGameDoSolutionLevelOne() {
		EyeballGame ebGame = new EyeballGame();
		ebGame.startGame(0);		
		ebGame.showGameSolution(0);
		assertTrue(ebGame.eb.getMyCurrentPiece().isEndPoint() == true);
	}
	
	@DisplayName("Test Game can do solution in level 2 ")
	@Test
	void testEyegallGameDoSolutionLevelTwo() {
		EyeballGame ebGame = new EyeballGame();
		ebGame.startGame(1);		
		ebGame.showGameSolution(1);
		assertTrue(ebGame.eb.getMyCurrentPiece().isEndPoint() == true);
	}
}
