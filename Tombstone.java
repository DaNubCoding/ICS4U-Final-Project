import greenfoot.Greenfoot;

/**
 * A... grave?
 *
 * @author Andrew Wang
 * @author Sandra Huang
 * @version May 2024
 */
public class Tombstone extends Feature {
    /**
     * Create a new Tombstone.
     */
    public Tombstone(FeatureData data) {
        super("tombstone", data);
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        getWorld().addCollisionController(new CollisionController(this, 4, 0.3, 1.0));
    }
}
