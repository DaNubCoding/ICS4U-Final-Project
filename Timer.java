/**
 * A timer object that keeps track of a set amount of time in frames.
 * <p>
 * Example usage:
 * <pre>
 * if (timer.ended()) {
 *     // Do things
 *     timer.restart();
 * }
 * </pre>
 *
 * @author Andrew Wang
 * @version April 2024
 */
public class Timer {
    // The total number of acts that have occurred since reset
    private static int currentAct = 0;

    /**
     * Get the number of acts that have occurred in all worlds.
     *
     * @return the number of acts since reset
     */
    public static int getCurrentAct() {
        return currentAct;
    }

    /**
     * Increment the current act number.
     * <p>
     * Call this at the end of the world's act method.
     */
    public static void incrementAct() {
        currentAct++;
    }

    // The frame at which the timer begins
    private int startFrame;
    // The total number of frames before the timer ends
    private int totalFrames;

    /**
     * Initialize the Timer with a set number of frames before it ends.
     *
     * @param totalFrames the total number of frames before the timer ends
     */
    public Timer(int totalFrames) {
        this.startFrame = currentAct;
        this.totalFrames = totalFrames;
    }

    /**
     * Check if the Timer has ended (whether it has reached the specified number
     * of frames).
     *
     * @return true if ended, false otherwise
     */
    public boolean ended() {
        return currentAct - startFrame >= totalFrames;
    }

    /**
     * Get the progress of the Timer as a percentage (i.e. 20% would be 0.2).
     *
     * @return the progress as a percentage
     */
    public double progress() {
        return (double) (currentAct - startFrame) / totalFrames;
    }

    /**
     * Restart the Timer with the same total number of frames as before.
     */
    public void restart() {
        startFrame = currentAct;
    }

    /**
     * Restart the Timer with a new total number of frames before it ends.
     *
     * @param newTotalFrames the number of frames before the Timer ends again
     */
    public void restart(int newTotalFrames) {
        startFrame = currentAct;
        totalFrames = newTotalFrames;
    }
}
