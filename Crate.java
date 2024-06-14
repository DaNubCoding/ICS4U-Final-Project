import greenfoot.Greenfoot;
import java.util.List;
import java.util.Arrays;
import java.util.function.Supplier;

/**
 * A box that holds an item available for the player to collect by walking into it.
 *
 * @author Andrew Wang
 * @author Martin Baldwin
 * @version June 2024
 */
public class Crate extends Feature {
    private static final List<Supplier<Item>> itemSuppliers = Arrays.asList(
        Pistol::new,
        Sword::new,
        Pitchfork::new,
        Axe::new,
        Bat::new,
        WandOfManyCanopies::new,
        Hammer::new,
        Repeater::new,
        RPG::new,
        FlowerBoomerang::new
    );

    private final Item content;

    /**
     * Create a new Crate with specified id and random item content.
     */
    public Crate(FeatureData data) {
        this(data, itemSuppliers.get(Greenfoot.getRandomNumber(itemSuppliers.size())).get());
    }

    /**
     * Create a new Crate with specified id and item content.
     */
    public Crate(FeatureData data, Item content) {
        super("crate", data);
        this.content = content;
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        getWorld().addCollisionController(new CollisionController(this, 10, 1.0, 0.0));
    }

    @Override
    public void update() {
        if(getWorld().getPlayer().getWorldPos().distanceTo(getWorldPos()) <= 20) {
            getWorld().addWorldObject(content, getWorldPos());
            removeFromWorld();
        }
    }
}
