import java.util.List;
import java.util.ArrayList;
import java.util.Random;

/**
 * An invisible feature that spawns grass clusters
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class GrassSpawner extends Feature
{
    private Random rand;
    private int numGrass;
    private List<Grass> allGrass;
    public GrassSpawner(FeatureData data){
        super(null, data);
        rand = new Random(data.id);
        numGrass = rand.nextInt(10 - 1) + 1;
        allGrass = new ArrayList<Grass>();
        for(int i=0; i<numGrass; i++){
            allGrass.add(new Grass(rand.nextInt(4)));
        }
    }

    @Override
    public void addedToWorld(PixelWorld world){
        SprackWorld w = (SprackWorld) world;
        for(Grass grass : allGrass){
            w.addWorldObject(grass, getWorldX() + rand.nextInt(30) - 15, getWorldY(), getWorldZ() + rand.nextInt(30) - 15);
        }
    }
    @Override
    public void removedFromWorld(PixelWorld world){
        for(Grass grass : allGrass){
            world.removeSprite(grass);
        }
    }
}
