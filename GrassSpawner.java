import java.util.List;
import java.util.ArrayList;

/**
 * An invisible feature that spawns grass clusters
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class GrassSpawner extends Feature
{
    private int numGrass;
    private List<Grass> allGrass;
    public GrassSpawner(FeatureData data){
        super(null, data);
        numGrass = (int)(Math.random() * 10) + 1;
        allGrass = new ArrayList<Grass>();
        for(int i=0; i<numGrass; i++){
            allGrass.add(new Grass((int)(Math.random() * 3)));
        }
    }

    @Override
    public void addedToWorld(PixelWorld world){
        SprackWorld w = (SprackWorld) world;
        for(Grass grass : allGrass){
            w.addWorldObject(grass, getWorldX() + Math.random() * 30, getWorldY(), getWorldZ()  + Math.random() * 30);
        }
    }
    @Override
    public void removedFromWorld(PixelWorld world){
        for(Grass grass : allGrass){
            world.removeSprite(grass);
        }
    }
}
