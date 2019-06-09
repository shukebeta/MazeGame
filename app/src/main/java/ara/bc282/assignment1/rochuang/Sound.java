package ara.bc282.assignment1.rochuang;

//import java.applet.Applet;
//import java.applet.AudioClip;
import java.net.URL;

public class Sound {
	
	protected static boolean isSoundOn = false;			//updated from EyeballGame Class
	final static URL MOVE = Sound.class.getResource("././sounds/move.wav");
	final static URL ERROR = Sound.class.getResource("././sounds/error.wav");
	final static URL WINNING = Sound.class.getResource("././sounds/winning.wav");
	
	public static void playSound(URL urlSelection) {
		if (isSoundOn) {
//			AudioClip clip = Applet.newAudioClip(urlSelection);
//			clip.play();
			//clip.stop();
		}
	}
	public static void playMoveSound() { playSound(MOVE); }
	public static void playErrorSound() { playSound(ERROR); }
	public static void playWinningSound() {playSound(WINNING); }

}