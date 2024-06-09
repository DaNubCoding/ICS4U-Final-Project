import greenfoot.*;

/**
 * A willow tree.
 *
 * @author Andrew Wang
 * @author Sandra Huang
 * @version May 2024
 */
public class WillowTree extends Feature {
    private Sprack canopy;

    /**
     * Create a new Tree.
     */
    public WillowTree(FeatureData data) {
        super("tree_willow_trunk", data);
        setWorldRotation(Greenfoot.getRandomNumber(360));
        canopy = new TreeCanopy("tree_willow_canopy");
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        getWorld().addWorldObject(canopy, getWorldX(), getWorldY() + 29, getWorldZ());
        canopy.setWorldRotation(Greenfoot.getRandomNumber(360));
    }

    @Override
    public void removedFromWorld(PixelWorld world) {
        world.removeSprite(canopy);
    }
}
