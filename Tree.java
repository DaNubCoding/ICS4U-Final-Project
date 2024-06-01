import greenfoot.*;

/**
 * TODO: add documentation for this and do some implementation
 * @author Lucas Fu
 * @version May 2024
 */
public class Tree extends Feature {
    /**
     * Create a new Tree.
     */
    public Tree(FeatureData data) {
        super("tree_trunk", data);
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        SpriteStackingWorld spriteStackingWorld = (SpriteStackingWorld) world;
        Sprack canopy = new TreeCanopy("tree_canopy");
        spriteStackingWorld.addSprack(canopy, getWorldX(), getWorldY() + 29, getWorldZ());
        canopy.setWorldRotation(Greenfoot.getRandomNumber(360));
    }
}
