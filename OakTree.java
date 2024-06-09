import greenfoot.*;

/**
 * Trees are your average world decoration. They're quite pretty!
 *
 * @author Lucas Fu
 * @author Sandra Huang
 * @version May 2024
 */
public class OakTree extends Feature {
    private Sprack canopy;

    /**
     * Create a new Tree.
     */
    public OakTree(FeatureData data) {
        super("tree_oak_trunk", data);
        setWorldRotation(Greenfoot.getRandomNumber(360));
        canopy = new TreeCanopy("tree_oak_canopy");
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        getWorld().addWorldObject(canopy, getWorldX(), getWorldY() + 29, getWorldZ());
        canopy.setWorldRotation(Greenfoot.getRandomNumber(360));
        getWorld().addCollisionController(new CollisionController(this, 3, 1.0, 1.0));
    }

    @Override
    public void removedFromWorld(PixelWorld world) {
        world.removeSprite(canopy);
    }
}
