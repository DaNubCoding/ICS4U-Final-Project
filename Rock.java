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

    @Override
    public void addedToWorld(PixelWorld world) {
        getWorld().addCollisionController(new CollisionController(this, 5, 0.3, 1.0));
    }
}
