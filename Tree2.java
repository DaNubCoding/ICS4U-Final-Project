import greenfoot.*;

public class Tree2 extends Feature {

    private Sprack canopy;

    /**
     * Create a new Tree.
     */
    public Tree2(FeatureData data) {
        super("tree_trunk2", data);
        setWorldRotation(Greenfoot.getRandomNumber(360));
        canopy = new TreeCanopy("tree_canopy2");
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
