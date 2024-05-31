import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.function.BiConsumer;

/**
 * A class that stores and handles all the data to be used inside the world.
 * TODO: determine spawn chance, maybe group spawns together?
 * @author Lucas Fu
 * @version May 2024
 */
public class WorldData {
    private Random worldRand;
    private static Random rand;

    // settings
    public static final int generationRadius = 5;
    private static final HashMap<Vector2, Feature> landmarks = new HashMap<>() {{
        put(new Vector2(0, 0), new Feature(0,"tower"));
        // more landmarks can be placed here
    }};

    // storage variables
    private long seed;
    private Vector2 playerLocation;
    private ArrayList<Long> modifiedElementIDs;
    private HashMap<Vector2, Feature> surroundings;

    /**
     * Create a new WorldData object with default settings.
     */
    public WorldData(){
        worldRand = new Random();
        seed = worldRand.nextLong();
        playerLocation = new Vector2(0, 0);
        surroundings = new HashMap<Vector2, Feature>();
        modifiedElementIDs = new ArrayList<Long>();
    }

    /**
     * Create a WorldData object from a seed.
     * <p>
     * This attempts to load a file using the seed number. If this fails to find
     * such a file, this creates a new WorldData with the specified seed.
     * @param seed the seed to be used when creating the WorldData
     */
    public WorldData(long seed){
        this();
        Scanner scf;
        try{
            scf = new Scanner(new File("saves/save_"+seed+".csv"));
            // get the seed
            this.seed = Long.valueOf(scf.nextLine());
            // get the player coordinates
            long x = scf.nextLong(), y = scf.nextLong(); scf.nextLine();
            playerLocation = new Vector2(x, y);
            // get the list of modified elements
            StringTokenizer st = new StringTokenizer(scf.nextLine(), ",");
            while(st.hasMoreTokens()){
                modifiedElementIDs.add(Long.valueOf(st.nextToken()));
            }
        } catch(FileNotFoundException e){
            this.seed = seed;
        }
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
                if(landmarks.containsKey(elementPos)){
                    surroundings.remove(elementPos);
                    surroundings.put(elementPos, landmarks.get(elementPos));
                }
                if(modifiedElementIDs.contains(localID)){
                    surroundings.get(elementPos).modify();
                }
            }
        }
    }

    private static Feature generateElement(long id){
        rand = new Random(id);
        int roll = rand.nextInt(100);
        for (int i = 0; i < Feature.Type.length(); i++){
            if (roll < Feature.Type.getSpawnRate(i)) {
                return Feature.Type.createFeature(i, id);
            }
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
    public boolean updatePlayerLocation(int x, int y, int radius, BiConsumer<WorldData, Vector2> adder, BiConsumer<WorldData, Vector2> remover){
        if (x == playerLocation.x && y == playerLocation.y) return false;
        if (x != playerLocation.x){
            boolean bigger = x > playerLocation.x;
            int topBorder = (int) Math.max(y, playerLocation.y) + radius;
            int btmBorder = (int) Math.min(y, playerLocation.y) - radius;
            double oldBorder = playerLocation.x - radius * (bigger ? 1 : -1);
            double newBorder = x + radius * (bigger ? 1 : -1);

            // add all elements on new border
            for (int i = (int) topBorder; i >= (int) btmBorder; i--){

                // initialize element information
                Vector2 elementPos = new Vector2(newBorder, i);
                adder.accept(this, elementPos);
            }

            // remove all elements on old border
            for (int i = topBorder + 2; i >= (int) btmBorder - 2; i--){
                remover.accept(this, new Vector2(oldBorder, i));
            }
        }
        if(y != playerLocation.y){
            boolean bigger = y > playerLocation.y;
            int leftBorder = (int) Math.min(x, playerLocation.x) - radius;
            int rightBorder = (int) Math.max(x, playerLocation.x) + radius;
            double oldBorder = playerLocation.y - radius * (bigger ? 1 : -1);
            double newBorder = y + radius * (bigger ? 1 : -1);

            // add all elements on new border
            for(int i = rightBorder; i >= leftBorder; i--){

                // initlize element information
                Vector2 elementPos = new Vector2(i, newBorder);
                adder.accept(this, elementPos);
            }

            // remove all elements on old border
            for(int i = rightBorder + 2; i >= leftBorder - 2; i--){
                remover.accept(this, new Vector2(i, oldBorder));
            }
        }

        // edge case where some terrain is left behind
        if(x != playerLocation.x && y != playerLocation.y){
            int leftBorder = (int) x - radius - 1;
            int rightBorder = (int) x + radius + 1;
            int topBorder = (int) y + radius + 1;
            int btmBorder = (int) y - radius - 1;

            // remove the top and bottom borders
            for(int i = leftBorder; i <= rightBorder; i++){
                remover.accept(this, new Vector2(i, topBorder));
                remover.accept(this, new Vector2(i, btmBorder));
            }

            // remove the left and right borders
            for(int i = btmBorder; i <= topBorder; i++){
                remover.accept(this, new Vector2(leftBorder, i));
                remover.accept(this, new Vector2(rightBorder, i));
            }
        }
        playerLocation = new Vector2(x, y);
        return true;
    }

    /**
     * Add a feature at the specified coordinate and apply necessary
     * modifications.
     *
     * @param data the WorldData object on which to operate
     * @param coord the coordinate of the {@link Feature} to add
     */
    public static void addFeature(WorldData data, Vector2 coord) {
        long localID = data.getSeed() + coord.getSzudzikValue();

        HashMap<Vector2, Feature> surroundings = data.getSurroundings();
        // add element to the list
        surroundings.put(coord, generateElement(localID));
        if(data.getModifiedElementIDs().contains(localID)){
            surroundings.get(coord).modify();
        }

        // replace element if it is a landmark
        if(landmarks.containsKey(coord)){
            surroundings.put(coord, landmarks.get(coord));
        }
    }

    /**
     * Remove a feature at the specified coordinates.
     *
     * @param coord the coordinate of the {@link Feature} to remove
     */
    public void removeFeature(Vector2 coord) {
        surroundings.remove(coord);
    }

    /**
     * Get the hashmap that represents elements surrounding the player.
     * @return the hashmap that represents elements surrounding the player
     */
    public HashMap<Vector2, Feature> getSurroundings(){
        return surroundings;
    }

    /**
     * Get the array of modified element IDs.
     *
     * @return the modified element IDs
     */
    public ArrayList<Long> getModifiedElementIDs() {
        return modifiedElementIDs;
    }

    /**
     * Get the seed of the world.
     *
     * @return the seed of the world
     */
    public long getSeed() {
        return seed;
    }

    /**
     * Save the data to a csv file called save_{seed}.
     * <p>
     * The file contains the seed, the player location, and the modified elements.
     */
    public void saveData(){
        PrintWriter fileOutput;
        try {
            fileOutput = new PrintWriter(new FileWriter("saves/save_"+seed+".csv"));
        } catch (IOException e) {
            System.out.println(e);
            return;
        }
        fileOutput.println(seed);
        fileOutput.printf("%d,%d\n", playerLocation.x, playerLocation.y);
        for(long l : modifiedElementIDs){
            fileOutput.print(l+",");
        }
        fileOutput.close();
    }
}
