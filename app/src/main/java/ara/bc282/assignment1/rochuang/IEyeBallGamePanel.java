package ara.bc282.assignment1.rochuang;

public abstract interface IEyeBallGamePanel {
	public abstract void startGame(int newGameLevel);	//Start button
	public abstract void reStartGame(int newGameLevel);	// Restart button
	public abstract void showGameSolution(int newGameLevel); //Show Solution button
	public abstract void alertGameMessage();	//Game Alert Message
	public abstract void selectSoundOn(boolean isSoundOn);	//Sound Selection
}
