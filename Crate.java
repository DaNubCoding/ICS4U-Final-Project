import greenfoot.Greenfoot;
import java.util.Random;

/**
 * ?????
 *
 * @author Andrew Wang
 * @version May 2024
 */
public class Crate extends Feature {
    private Weapon[] contents = new Weapon[] {
        new Pistol(null),
        new Sword(null),
        new EnderPearlGun(null)
    };
    Random rand = new Random();

    /**
     * Create a new Crate with specified id.
     */
    public Crate(FeatureData data) {
        super("crate", data);
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        getWorld().addCollisionController(new CollisionController(this, 10, 1.0, 0.0));
    }

    @Override
    public void update() {
    }
}
