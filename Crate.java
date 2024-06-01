import greenfoot.Greenfoot;

/**
 * ?????
 *
 * @author Andrew Wang
 * @version May 2024
 */
public class Crate extends Feature {
    /**
     * Create a new Crate with specified id.
     */
    public Crate(FeatureData data) {
        super("crate", data);
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }
}
