import java.util.Random;

/**
 * Spawns dirt
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class DirtSpawner extends Feature
{
    private Random random;
    private Dirt dirt;

    public DirtSpawner(FeatureData data) {
        super(null, data);
        random = new Random(data.id);
        dirt = new Dirt(random.nextInt(5), random.nextInt(360));
    }

    @Override
    public void addedToWorld(PixelWorld world){
        ((SprackWorld) world).addWorldObject(dirt, getWorldX(), getWorldY(), getWorldZ());
    }

    @Override
    public void removedFromWorld(PixelWorld world){
        world.removeSprite(dirt);
    }
}
