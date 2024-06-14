import greenfoot.GreenfootSound;

/**
 * Handling for world-independent looping background music.
 *
 * @author Martin Baldwin
 * @version April 2024
 */
public final class Music {
    // Don't let anyone instantiate this class
    private Music() {}

    // The current sound used as background music
    private static GreenfootSound sound = null;
    // The file path of the above sound
    private static String soundPath = null;

    // Whether or not to be handling the background music, corresponding to
    // whether or not the program is running.
    private static boolean isRunning = false;

    /**
     * Set the current background music to the given sound file, with optional
     * looping.
     * <p>
     * If the given file is the same as the one currently playing, this method
     * does not reset the music. Otherwise, the currently playing background
     * music, if any, will be stopped.
     * <p>
     * The sound will automatically be started only if the program is running.
     *
     * @param soundPath the file path of a sound file to play as background music
     * @param loop true to use {@link GreenfootSound#playLoop}, false to use {@link GreenfootSound#play}
     */
    public static void set(String soundPath, boolean loop) {
        if (soundPath != Music.soundPath) {
            if (sound != null) {
                sound.stop();
            }
            sound = new GreenfootSound(soundPath);
            Music.soundPath = soundPath;
            sound.stop();
        }
        if (isRunning) {
            if (loop) {
                sound.playLoop();
            } else {
                sound.play();
            }
        }
    }

    /**
     * Set the current background music to a loop of the given sound file.
     *
     * @param soundPath the file path of a sound file to play as background music
     * @see set(String, boolean)
     */
    public static void set(String soundPath) {
        set(soundPath, true);
    }

    /**
     * Continue playing the current background music, if any.
     */
    public static void play() {
        isRunning = true;
        if (sound == null) {
            return;
        }
        sound.playLoop();
    }

    /**
     * Pause the currently playing background music, if any.
     */
    public static void pause() {
        isRunning = false;
        if (sound == null) {
            return;
        }
        sound.pause();
    }

    /**
     * Stop playing the current background music, if any, and unset the sound
     * used so that future attempts to play do nothing.
     */
    public static void stop() {
        if (sound == null) {
            return;
        }
        sound.stop();
        sound = null;
        soundPath = null;
    }
}
