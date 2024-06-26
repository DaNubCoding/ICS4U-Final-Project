import java.util.Random;

/**
 * A Feature that creates {@link Pond}s.
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class PondSpawner extends Feature
{
    private Random random;
    private Pond pond;

    public PondSpawner(FeatureData data) {
        super(null, data);
        random = new Random(data.id);
        pond = new Pond(random, random.nextInt(3), random.nextInt(360));
    }

    @Override
    public void addedToWorld(PixelWorld world){
        ((SprackWorld) world).addWorldObject(pond, getWorldX(), getWorldY(), getWorldZ());
    }

    @Override
    public void removedFromWorld(PixelWorld world){
        world.removeSprite(pond);
    }
}
