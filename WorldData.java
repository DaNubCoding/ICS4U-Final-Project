import java.util.ArrayList;
import java.util.HashMap;
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
    private ArrayList<Long> modifiedElementIDs;
    private HashMap<Vector2, WorldElement> surroundings;

    /**
     * Create a new WorldData class with default settings.
     */
    public WorldData(){
        worldRand = new Random();
        seed = worldRand.nextLong();
        playerLocation = new Vector2(0, 0);
        surroundings = new HashMap<Vector2, WorldElement>();
        modifiedElementIDs = new ArrayList<Long>();
    }

    /**
     * Generate the world around the player in a fixed radius.
     * <p>
     * This should only be used when initially generating the world.
     */
    public void generateWorld(){
        for(int i = 0; i < (2 * generationRadius + 1); i++){
            for(int j = 0; j < (2 * generationRadius + 1); j++){
                int dx = j - generationRadius;
                int dy = i - generationRadius;
                Vector2 elementPos = playerLocation.add(new Vector2(dx, dy));
                long localID = seed + elementPos.getSzudzikValue();
                surroundings.put(elementPos, generateElement(localID));
                if(modifiedElementIDs.contains(localID)){
                    surroundings.get(elementPos).modify();
                }
            }
        }
    }

    private WorldElement generateElement(long id){
        rand = new Random(id);
        int roll = rand.nextInt(100);
        if(roll < treePercentChance){
            return new Tree(id);
        }
        return null;
    }

    /**
     * Update the player location, freeing up the unloaded terrain and generating
     * new terrain.
     * @param x the x-value of the grid location of the new player location
     * @param y the y-value of grid location of the new player location
     * @return false if the update did not affect anything, true otherwise
     */
    public boolean updatePlayerLocation(int x, int y){
        if (x == playerLocation.x && y == playerLocation.y) return false;
        if (x != playerLocation.x){
            boolean bigger = x > playerLocation.x;
            int topBorder = (int) Math.max(y, playerLocation.y) + generationRadius;
            int btmBorder = (int) Math.min(y, playerLocation.y) - generationRadius;
            double oldBorder = playerLocation.x - generationRadius * (bigger ? 1 : -1);
            double newBorder = x + generationRadius * (bigger ? 1 : -1);

            // add all elements on new border
            for (int i = (int) topBorder; i >= (int) btmBorder; i--){

                // initialize element information
                Vector2 elementPos = new Vector2(newBorder, i);
                long localID = seed + elementPos.getSzudzikValue();

                // add element to the list
                surroundings.put(elementPos, generateElement(localID));
                if(modifiedElementIDs.contains(localID)){
                    surroundings.get(elementPos).modify();
                }
            }
            
            // remove all elements on old border
            for (int i = topBorder + 2; i >= (int) btmBorder - 2; i--){
                surroundings.remove(new Vector2(oldBorder, i));
            }
        }
        if(y != playerLocation.y){
            boolean bigger = y > playerLocation.y;
            int leftBorder = (int) Math.min(x, playerLocation.x) - generationRadius;
            int rightBorder = (int) Math.max(x, playerLocation.x) + generationRadius;
            double oldBorder = playerLocation.y - generationRadius * (bigger ? 1 : -1);
            double newBorder = y + generationRadius * (bigger ? 1 : -1);

            // add all elements on new border
            for(int i = rightBorder; i >= leftBorder; i--){

                // initlize element information
                Vector2 elementPos = new Vector2(i, newBorder);
                long localID = seed + elementPos.getSzudzikValue();

                // add element to the list
                surroundings.put(elementPos, generateElement(localID));
                if(modifiedElementIDs.contains(localID)){
                    surroundings.get(elementPos).modify();
                }
            }

            // remove all elements on old border
            for(int i = rightBorder + 2; i >= leftBorder - 2; i--){
                surroundings.remove(new Vector2(i, oldBorder));
            }
        }

        // edge case where some terrain is left behind
        if(x != playerLocation.x && y != playerLocation.y){
            int leftBorder = (int) x - generationRadius - 1;
            int rightBorder = (int) x + generationRadius + 1;
            int topBorder = (int) y + generationRadius + 1;
            int btmBorder = (int) y - generationRadius - 1;

            // remove the top and bottom borders
            for(int i = leftBorder; i <= rightBorder; i++){
                surroundings.remove(new Vector2(i, topBorder));
                surroundings.remove(new Vector2(i, btmBorder));
            }

            // remove the left and right borders
            for(int i = btmBorder; i <= topBorder; i++){
                surroundings.remove(new Vector2(leftBorder, i));
                surroundings.remove(new Vector2(rightBorder, i));
            }
        }
        playerLocation = new Vector2(x, y);
        return true;
    }

    /**
     * Get the hashmap that represents elements surrounding the player.
     * @return the hashmap that represents elements surrounding the player
     */
    public HashMap<Vector2, WorldElement> getSurroundings(){
        return surroundings;
    }
}
