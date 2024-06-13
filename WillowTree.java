import java.util.List;

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
        getWorld().addCollisionController(new CollisionController(this, 3, 1.0, 1.0));
    }

    @Override
    public void update() {
        super.update();
        List<? extends Sprite> ponds = getWorld().getSprites(Pond.class);
        for (Sprite s : ponds){
            Pond pond = (Pond) s;
            if (pond.getWorldPos().distanceTo(getWorldPos()) < pond.getSize() / 2){
                removeFromWorld();
                break;
            }
        }
    }

    @Override
    public void removedFromWorld(PixelWorld world) {
        world.removeSprite(canopy);
    }
}
