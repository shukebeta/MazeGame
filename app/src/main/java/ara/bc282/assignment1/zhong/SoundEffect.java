package ara.bc282.assignment1.zhong;

public class SoundEffect {

    private static boolean soundOn = true;
    private final static String WARNING = "./sounds/450617__breviceps__8-bit-time-s-up.wav";
    private final static String SUCCEED_MOVE = "./sounds/448265__henryrichard__sfx-correct.wav";
    private final static String CONGRATULATIONS = "./sounds/433702__dersuperanton__congratulations-deep-voice.wav";


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
