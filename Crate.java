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
        // new Pistol(),
        // new Sword(),
        // new Pitchfork(),
        // new Axe(),
        // new Bat(),
        new WandOfManyCanopies()
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
        if(getWorld().getPlayer().getWorldPos().distanceTo(getWorldPos()) <= 20) {
            getWorld().addWorldObject(contents[rand.nextInt(contents.length)], getWorldPos());
            removeFromWorld();
        }
    }
}
