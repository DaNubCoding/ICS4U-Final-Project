import greenfoot.*;
/**
 * Boulders are a form of background decoration with a collision box.
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Boulder extends Feature
{
    public Boulder(FeatureData data) {
        super("boulder", data);
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        getWorld().addCollisionController(new CollisionController(this, 10, 0.5, 1.0));
    }
}
