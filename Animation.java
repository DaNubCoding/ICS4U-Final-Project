/**
 * A general-purpose animation that loops through a list of string names over
 * time, controlled by the number of calls to its {@link #update()} method.
 * <p>
 * Each cel of an animation has a string name and an integer frame duration,
 * where a given name will be returned by {@link #getCurrentName()} after that
 * integer number of calls to {@code update()}.
 * <p>
 * For a cel to last forever (which will thus never allow the animation to
 * advance further), it may have the frame duration value of -1.
 *
 * @author Martin Baldwin
 * @version June 2024
 */
public class Animation {
    // Names for all cels in this animation
    private final String[] names;
    // Frame durations for corresponding cels in names
    private final int[] frames;

    // Whether or not the current name was just changed by update()
    private boolean hasChanged;
    // Whether or not the current name just returned to the first name after update()
    private boolean hasLooped;

    // Current index of the current cel in names
    private int currentIndex;
    // Number of frames remaining for the current cel
    private int counter;

    /**
     * Create a new animation consisting of the given names each lasting a
     * constant number of frames.
     * <p>
     * If the given frame value is -1, the cels will last forever. This
     * effectively makes the animation a still image of the first cel.
     *
     * @param frame the duration of all cels in names
     * @param names the string names representing each cel of the animation in order
     */
    public Animation(int frame, String... names) {
        this.names = names;
        frames = new int[names.length];
        for (int i = 0; i < frames.length; i++) {
            frames[i] = frame;
        }
        reset();
    }

    /**
     * Create a new animation consisting of the given names each lasting an
     * individually specified number of frames.
     * <p>
     * If a value in frames is -1, its corresponding cel will last forever.
     *
     * @param names the string names representing each cel of the animation in order
     * @param frames the duration of each corresponding cel in names
     * @throws IllegalArgumentException if names and frames are of differing lengths
     */
    public Animation(String[] names, int[] frames) {
        if (names.length != frames.length) {
            throw new IllegalArgumentException("Animation names and frames length mismatch");
        }
        this.names = names;
        this.frames = frames;
        reset();
    }

    /**
     * Reset this animation to its beginning.
     * <p>
     * The value returned by {@link #hasChanged()} will be true after calling
     * this method.
     * <p>
     * The value returned by {@link #hasLooped()} will be false after calling
     * this method.
     */
    public void reset() {
        hasChanged = true;
        hasLooped = false;
        if (names.length > 0) {
            currentIndex = 0;
            counter = frames[currentIndex];
        } else {
            currentIndex = -1;
            counter = -1;
        }
    }

    /**
     * Update this animation, signalling an advancement of one frame.
     * <p>
     * The value returned by {@link #hasChanged()} will be updated by this
     * accordingly.
     * <p>
     * The value returned by {@link #hasLooped()} will be updated by this
     * accordingly.
     */
    public void update() {
        if (currentIndex == -1 || counter == -1) {
            hasChanged = false;
            hasLooped = false;
            return;
        }
        if (--counter == 0) {
            currentIndex++;
            if (currentIndex == names.length) {
                currentIndex = 0;
                hasLooped = true;
            } else {
                hasLooped = false;
            }
            counter = frames[currentIndex];
            hasChanged = true;
        } else {
            hasChanged = false;
            hasLooped = false;
        }
    }

    /**
     * Test if this animation's cel has just been changed by the
     * {@link #update()} method.
     *
     * @return true if the current cel differs from the previous cel, false otherwise
     */
    public boolean hasChanged() {
        return hasChanged;
    }

    /**
     * Test if this animation's cel has just been changed by the
     * {@link #update()} method to the first cel.
     *
     * @return true if the current cel is the first cel of the animation and it differs from the previous cel, false otherwise
     */
    public boolean hasLooped() {
        return hasLooped;
    }

    /**
     * Return the name of the current cel of this animation.
     *
     * @return the string name representing the current cel of this animation.
     */
    public String getCurrentName() {
        return names[currentIndex];
    }
}
