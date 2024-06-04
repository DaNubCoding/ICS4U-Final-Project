import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Random;
import java.util.Scanner;
import java.util.StringTokenizer;
import java.util.function.BiConsumer;
import java.util.List;

/**
 * A class that stores and handles all the data to be used inside the world.
 *
 * @author Lucas Fu
 * @version May 2024
 */
public class WorldData {
    private Random worldRand;
    private static Random rand;

    // settings
    private static final int generationRadius = 15;
    private static final int emptyFeatureChance = 100;
    private static final HashMap<Vector2, Feature> landmarks = new HashMap<>() {{
        // put(new Vector2(0, 0), new Feature("tower"));
        // more landmarks can be placed here
    }};

    // storage variables
    private long seed;
    private Vector2 playerLocation;
    private HashMap<Long, FeatureData> modifiedFeatures;
    private HashMap<Vector2, Feature> surroundings;

    /**
     * Create a new WorldData object with default settings.
     */
    public WorldData() {
        worldRand = new Random();
        seed = worldRand.nextLong();
        playerLocation = new Vector2(0, 0);
        surroundings = new HashMap<Vector2, Feature>();
        modifiedFeatures = new HashMap<Long, FeatureData>();
    }

    /**
     * Create a WorldData object from a seed.
     * <p>
     * This attempts to load a file using the seed number. If this fails to find
     * such a file, this creates a new WorldData with the specified seed.
     *
     * @param seed the seed to be used when creating the WorldData
     */
    public WorldData(long seed) {
        this();
        Scanner scf;
        try{
            scf = new Scanner(new File("saves/save_" + seed + ".csv"));
            // get the seed
            this.seed = Long.valueOf(scf.nextLine());
            // get the player coordinates
            StringTokenizer st = new StringTokenizer(scf.nextLine(), ",");
            long x = Integer.valueOf(st.nextToken());
            long y = Integer.valueOf(st.nextToken());
            playerLocation = new Vector2(x, y);
            // get the list of modified elements
            while (scf.hasNextLine()) {
                st = new StringTokenizer(scf.nextLine(), ",");
                long id = Long.valueOf(st.nextToken());
                FeatureData featureData = new FeatureData(id);
                while (st.hasMoreTokens()) {
                    String key = st.nextToken();
                    String value = st.nextToken();
                    featureData.put(key, value);
                }
                modifiedFeatures.put(id, featureData);
            }
        } catch(FileNotFoundException e) {
            this.seed = seed;
        }
    }

    /**
     * Generate the world around the player in a fixed radius.
     * <p>
     * This should only be used when initially generating the world.
     */
    public void generateWorld() {
        // clusters
        for(int i = 0; i < (4 * generationRadius + 1); i++) {
            for(int j = 0; j < (4 * generationRadius + 1); j++) {
                int dx = j - 2 * generationRadius;
                int dy = i - 2 * generationRadius;
                Vector2 elementPos = playerLocation.add(new Vector2(dx, dy));
                addCluster(this, elementPos);
            }
        }

        // features
        for(int i = 0; i < (2 * generationRadius + 1); i++) {
            for(int j = 0; j < (2 * generationRadius + 1); j++) {
                int dx = j - generationRadius;
                int dy = i - generationRadius;
                Vector2 elementPos = playerLocation.add(new Vector2(dx, dy));
                addFeature(this, elementPos);
            }
        }
    }

    private static Feature generateFeature(WorldData data, long id, Vector2 coord, FeatureData featureData) {
        rand = new Random(id);

        // init spawn rates
        int[] spawnRates = new int[Feature.Type.length()];

        int i = 0;
        // spawn rate for the features
        for(Feature.Type type : Feature.Type.values()) {

            // init object's spawn rate
            if(i == 0) spawnRates[0] = type.spawnRate;
            else spawnRates[i] = spawnRates[i - 1] + type.spawnRate;

            List<Vector2> centers = Cluster.getCenter(type.cls);
            if (centers != null) {
                Cluster clusterType = Cluster.getFromFeature(type);
                for (Vector2 center : centers) {
                    // A multiplier that decreases the spawn rate of the feature
                    // based on the distance from the center
                    int closenessMult = Math.max(clusterType.maxRadius - (int) center.distanceTo(coord), 0);
                    // Apply this multiplier to the defined density of features in
                    // the cluster
                    spawnRates[i] += closenessMult * clusterType.density;
                }
            }

            i++;
        }

        // maximum roll value is increased by the fixed empty weight
        int roll = rand.nextInt(spawnRates[spawnRates.length-1] + emptyFeatureChance);

        // apply spawn rate
        for (int j = 0; j < Feature.Type.length(); j++) {
            if (roll < spawnRates[j]) {
                return Feature.Type.createFeature(j, featureData);
            }
        }

        // if nothing spawned, return null
        return null;
    }

    private static Cluster generateCluster(long id) {
        rand = new Random(id);
        // clusters currently use a probability system
        int roll = rand.nextInt(500);
        for(Cluster cluster : Cluster.values()) {
            if(roll < cluster.spawnRate) {
                return cluster;
            }
        }
        return null;
    }

    /**
     * Update features, freeing up the unloaded terrain and generating new terrain.
     * @param dx the change in x-value of the grid location of the player location
     * @param dy the change in y-value of grid location of the player location
     * @param radius the radius in which the update takes place
     * @param adder the operation used to add a feature
     * @param remove the operation used to remove a feature
     */
    private void updateFeatures(int dx, int dy, int radius, BiConsumer<WorldData, Vector2> adder, BiConsumer<WorldData, Vector2> remover) {
        double x = playerLocation.x + dx;
        double y = playerLocation.y + dy;
        int topBorder = (int) Math.max(y, playerLocation.y) + radius;
        int btmBorder = (int) Math.min(y, playerLocation.y) - radius;
        int leftBorder = (int) Math.min(x, playerLocation.x) - radius;
        int rightBorder = (int) Math.max(x, playerLocation.x) + radius;

        if (dx != 0) {
            boolean bigger = dx > 0;
            double oldBorder = playerLocation.x - radius * (bigger ? 1 : -1);
            double newBorder = x + radius * (bigger ? 1 : -1);

            // add all elements on new border
            for (int i = (int) topBorder; i >= (int) btmBorder; i--) {

                // initialize element information
                Vector2 elementPos = new Vector2(newBorder, i);
                adder.accept(this, elementPos);
            }

            // remove all elements on old border
            for (int i = topBorder; i >= (int) btmBorder; i--) {
                remover.accept(this, new Vector2(oldBorder, i));
            }
        }
        if(dy != 0) {
            boolean bigger = dy > 0;
            double oldBorder = playerLocation.y - radius * (bigger ? 1 : -1);
            double newBorder = y + radius * (bigger ? 1 : -1);

            // add all elements on new border
            for(int i = rightBorder; i >= leftBorder; i--) {

                // initlize element information
                Vector2 elementPos = new Vector2(i, newBorder);
                adder.accept(this, elementPos);
            }

            // remove all elements on old border
            for(int i = rightBorder; i >= leftBorder; i--) {
                remover.accept(this, new Vector2(i, oldBorder));
            }
        }

        // edge case where some terrain is left behind
        if(x != playerLocation.x && y != playerLocation.y) {
            leftBorder = (int) x - radius - 1;
            rightBorder = (int) x + radius + 1;
            topBorder = (int) y + radius + 1;
            btmBorder = (int) y - radius - 1;

            // remove the top and bottom borders
            for(int i = leftBorder; i <= rightBorder; i++) {
                remover.accept(this, new Vector2(i, topBorder));
                remover.accept(this, new Vector2(i, btmBorder));
            }

            // remove the left and right borders
            for(int i = btmBorder; i <= topBorder; i++) {
                remover.accept(this, new Vector2(leftBorder, i));
                remover.accept(this, new Vector2(rightBorder, i));
            }
        }
    }

    /**
     * Update the player location and the surrounding features.
     * @param x the new player x-location
     * @param y the new player y-location
     * @return whether the update affected anything
     */
    public boolean updatePlayerLocation(int x, int y) {
        if(x == playerLocation.x && y == playerLocation.y) return false;
        updateFeatures(
            (int) (x - playerLocation.x),
            (int) (y - playerLocation.y),
            2 * generationRadius,
            WorldData::addCluster,
            WorldData::removeCluster
        );
        updateFeatures(
            (int) (x - playerLocation.x),
            (int) (y - playerLocation.y),
            generationRadius,
            WorldData::addFeature,
            WorldData::removeFeature
        );
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
    private static void addFeature(WorldData data, Vector2 coord) {
        long localID = data.getSeed() + coord.getSzudzikValue();

        HashMap<Vector2, Feature> surroundings = data.getSurroundings();

        // add landmark if possible
        if(landmarks.containsKey(coord)) {
            surroundings.put(coord, landmarks.get(coord));
            return;
        }

        // check if feature has modified data
        FeatureData featureData = data.getModifiedFeatures().get(localID);
        // Empty feature data if has not been modified
        if (featureData == null) {
            featureData = new FeatureData(localID, coord);
        }
        featureData.setPosition(coord);

        // generate feature and add it to the list
        Feature feature = generateFeature(data, localID, coord, featureData);
        if (feature != null) {
            surroundings.put(coord, feature);
        }
    }

    /**
     * Add an invisible cluster marker at the specified coordinate.
     *
     * @param data the WorldData object on which to operate
     * @param coord the coordinate of the {@link Cluster} to add
     */
    private static void addCluster(WorldData data, Vector2 coord) {
        long localID = data.getSeed() + coord.getSzudzikValue();
        Cluster toBeAdded = generateCluster(localID);
        if(toBeAdded != null) {
            Cluster.addCenter(toBeAdded.cls, coord);
        }
    }

    /**
     * Remove a feature at the specified coordinate.
     * <p>
     * This is used to manually remove features, like when the player destroys
     * a crate.
     *
     * @param coord the coordinate of the {@link Feature} to remove
     */
    public void removeFeature(Vector2 coord) {
        surroundings.remove(coord);
    }

    private void removeCluster(Vector2 coord) {
        Cluster.removeCenter(coord);
    }

    /**
     * Add a feature to the modified features list.
     *
     * @param featureData the data of the feature to add
     */
    public void addModified(FeatureData featureData) {
        modifiedFeatures.put(featureData.id, featureData);
    }

    /**
     * Get the hashmap that represents elements surrounding the player.
     * @return the hashmap that represents elements surrounding the player
     */
    public HashMap<Vector2, Feature> getSurroundings() {
        return surroundings;
    }

    /**
     * Get the array of modified element IDs.
     *
     * @return the modified element IDs
     */
    public HashMap<Long, FeatureData> getModifiedFeatures() {
        return modifiedFeatures;
    }

    /**
     * Get the seed of the world.
     *
     * @return the seed of the world
     */
    public long getSeed() {
        return seed;
    }

    public Vector2 getPlayerLocation() {
        return playerLocation;
    }

    public int getGenerationRadius() {
        return generationRadius;
    }

    /**
     * Save the data to a csv file called save_{seed}.
     * <p>
     * The file contains the seed, the player location, and the modified elements.
     */
    public void saveData() {
        PrintWriter fileOutput;
        try {
            fileOutput = new PrintWriter(new FileWriter("saves/save_"+seed+".csv"));
        } catch (IOException e) {
            System.out.println(e);
            return;
        }
        fileOutput.println(seed);
        fileOutput.println((int) (playerLocation.x) + "," + (int) playerLocation.y);
        for (FeatureData featureData : modifiedFeatures.values()) {
            fileOutput.println(featureData.toString());
        }
        fileOutput.close();
    }
}
