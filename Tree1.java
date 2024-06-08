import greenfoot.*;

/**
 * Trees are your average world decoration. They're quite pretty!
 * @author Lucas Fu
 * @version May 2024
 */
public class Tree1 extends Feature {
    private Sprack canopy;

    /**
     * Create a new Tree.
     */
    public Tree1(FeatureData data) {
        super("tree_trunk1", data);
        setWorldRotation(Greenfoot.getRandomNumber(360));
        canopy = new TreeCanopy("tree_canopy1");
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
