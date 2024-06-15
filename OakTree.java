import java.util.List;
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
        if (data.containsKey("stumped")) {
            setLoopingAnimation(new Animation(-1, "stump"));
            return;
        }
        canopy = new TreeCanopy("tree_oak_canopy");
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        // if it is already stumped
        if (canopy != null) {
            getWorld().addWorldObject(canopy, getWorldX(), getWorldY() + 29, getWorldZ());
            canopy.setWorldRotation(Greenfoot.getRandomNumber(360));
        }
        getWorld().addCollisionController(new CollisionController(this, 3, 1.0, 1.0));
    }

    @Override
    public void update() {
        List<? extends Sprite> ponds = getWorld().getSprites(Pond.class);
        for (Sprite s : ponds){
            Pond pond = (Pond) s;
            if (pond.getWorldPos().distanceTo(getWorldPos()) < pond.getSize() / 2){
                super.removeFromWorld();
                break;
            }
        }
    }

    @Override
    public void removeFromWorld() {
        if (getData().containsKey("stumped")) {
            super.removeFromWorld();
            return;
        }
        modify("stumped", null);
        setLoopingAnimation(new Animation(-1, "stump"));
        getWorld().removeSprite(canopy);

        if (Math.random() < 0.08) {
            getWorld().addWorldObject(new WandOfManyCanopies(), getWorldPos());
        }
    }

    @Override
    public void removedFromWorld(PixelWorld world) {
        if (getData().containsKey("stumped")) return;
        world.removeSprite(canopy);
    }
}
