package ara.bc282.assignment1.zhong;

public class SoundEffect {

    private static boolean soundOn = true;
    private final static String WARNING = "./sounds/warning.wav";
    private final static String SUCCEED_MOVE = "./sounds/succeed.wav";
    private final static String CONGRATULATIONS = "./sounds/congratulations.wav";


    private static void play(String sndFile) {
        if (soundOn) {
            String audioFilePath = sndFile;
            AudioPlayer player = new AudioPlayer();
            //player.play(audioFilePath);
        }
    }

    public static void playWarning() {
        play(WARNING);
    }

    public static void playSucceedMove() {
        play(SUCCEED_MOVE);
    }

    public static void playCongratulations() {
        play(CONGRATULATIONS);
    }

    public static boolean isSoundOn() {
        return soundOn;
    }

    public static void setSoundOn(boolean soundOn) {
        SoundEffect.soundOn = soundOn;
    }
}
