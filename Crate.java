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
     * @param id the unique id that is given to this crate
     */
    public Crate() {
        super("crate");
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }
}
