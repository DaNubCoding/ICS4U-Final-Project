import greenfoot.*;
/**
 * Spawns dirt
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class DirtSpawner extends Feature
{
    private Dirt dirt;
    public DirtSpawner(FeatureData data) {
        super(null, data);
        dirt = new Dirt(Greenfoot.getRandomNumber(5));
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
