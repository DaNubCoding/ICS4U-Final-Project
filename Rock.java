import greenfoot.*;
/**
 * A rough and edgy music genre
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Rock extends Feature
{
    public Rock(FeatureData data) {
        super("rock", data);
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }
}
