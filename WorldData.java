import java.util.ArrayList;
import java.util.Random;

/**
 * A class that stores and handles all the data to be used inside the world.
 * TODO: get some actual documentation in
 * @author Lucas Fu
 * @version May 2024
 */
public class WorldData {
    private Random worldRand;
    private Random rand;
    
    // settings
    private final int generationRadius = 5;
    private final int treePercentChance = 10;

    // storage variables
    private long seed;
    private Vector2 playerLocation;
    private WorldElement[][] surroundings = new WorldElement[2*generationRadius+1][2*generationRadius+1];
    private ArrayList<Long> modifiedElementIDs;

    /**
     * Create a new WorldData class with default settings.
     */
    public WorldData(){
        worldRand = new Random();
        seed = worldRand.nextLong();
        playerLocation = new Vector2(0, 0);
    }

    /**
     * Generate the world around the player in a fixed radius.
     */
    public void generateWorld(){
        for(int i=0; i<(surroundings.length); i++){
            for(int j=0; j<(surroundings[i].length); j++){
                int dx = j - generationRadius;
                int dy = i - generationRadius;
                long localID = seed + playerLocation.add(new Vector2(dx, dy)).getSzudzikValue();
                surroundings[i][j] = generateElement(localID);
                if(modifiedElementIDs.contains(localID)){
                    surroundings[i][j].modify();
                }
            }
        }
    }

    private WorldElement generateElement(long id){
        rand = new Random(id);
        int roll = rand.nextInt(100);
        if(roll<treePercentChance){
            return new Tree(id);
        }
        return new WorldElement(id, null);
    }
}
