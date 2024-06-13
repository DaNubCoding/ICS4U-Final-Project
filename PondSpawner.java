import greenfoot.*;
/**
 * Spawns ponds
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class PondSpawner extends Feature
{
    private Pond pond;
    public PondSpawner(FeatureData data) {
        super(null, data);
        pond = new Pond(Greenfoot.getRandomNumber(3));
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
