/**
 * Features are permanent spracks of the world, generated using {@link WorldData}.
 * <p>
 * Subclass this class to create a new type of Feature. To give it a texture,
 * add a new entry to {@link SprackView}, and pass the name of the entry to the
 * constructor of the superclass.
 * <p>
 * In order to spawn a new Feature in the world, add a new entry to {@link Type}
 * and pass in the class of the Feature, the constructor of the Feature (
 * {@code MyFeature::new}), and the spawn rate of the Feature.
 * <p>
 * To spawn the new Feature in clusters, see {@link Cluster}.
 *
 * @author Lucas Fu
 * @version May 2024
 */
public class Feature extends Sprack {
    /**
     * A functional interface that creates a new Feature with the given id.
     *
     * @author Andrew Wang
     * @version May 2024
     */
    @FunctionalInterface
    public interface FeatureFactory {
        /**
         * Create a new Feature with the given data.
         *
         * @return a new Feature with the given data
         */
        public Feature create(FeatureData data);
    }

    /**
     * An enum that represents the different types of features that can be
     * spawned, along with their spawnrates.
     *
     * @author Andrew Wang
     * @version May 2024
     */
    public static enum Type {
        OAK_TREE(OakTree.class, OakTree::new, 6),
        WILLOW_TREE(WillowTree.class, WillowTree::new, 3),
        CRATE(Crate.class, Crate::new, 20),
        TOMBSTONE(Tombstone.class, Tombstone::new, 10),
        ;

        private static int[] spawnRates = new int[Type.length()];

        static {
            spawnRates[0] = values()[0].spawnRate;
            for (int i = 0; i < Type.length(); i++) {
                spawnRates[i] = values()[i].spawnRate;
            }
        }

        /**
         * Get the number of different types of features.
         *
         * @return the number of different types of features
         */
        public static int length() {
            return values().length;
        }

        /**
         * Create a new Feature of the given type.
         *
         * @param i the index of the type to create
         * @param data the data to create the Feature with
         * @return a new Feature of the given type
         */
        public static Feature createFeature(int i, FeatureData data) {
            return values()[i].factory.create(data);
        }

        /**
         * The class of this type of Feature.
         */
        public final Class<? extends Feature> cls;
        /**
         * The factory used to create a new Feature of this type.
         */
        public final FeatureFactory factory;
        /**
         * The spawn rate of this type of Feature.
         */
        public final int spawnRate;

        private Type(Class<? extends Feature> cls, FeatureFactory factory, int spawnRate) {
            this.cls = cls;
            this.factory = factory;
            this.spawnRate = spawnRate;
        }
    }

    private FeatureData data;

    /**
     * Create a new Feature with the given id and the given sheet name.
     * <p>
     * Refer to {@link Sprack#Sprack}
     *
     * @param sheetName the name of the SprackView to use
     * @param data the data to create the Feature with
     */
    public Feature(String sheetName, FeatureData data) {
        super(sheetName);
        this.data = data;
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        if (data.containsKey("removed")) {
            getWorld().removeSprite(this);
        }
    }

    /**
     * Modify the data of this Feature.
     * <p>
     * For example
     *
     * @param key the key to modify
     * @param value the value to set the key to
     */
    public void modify(String key, Object value) {
        data.put(key, value);
        getWorld().getWorldData().addModified(data);
    }

    /**
     * Remove this Feature from the world.
     */
    public void removeFromWorld() {
        getWorld().removeSprite(this);
        getWorld().getWorldData().removeFeature(data.getPosition());
        modify("removed", null);
    }

    /**
     * Get the data of this Feature.
     *
     * @return the data of this Feature
     */
    public FeatureData getData() {
        return data;
    }
}
