/**
 * TODO: a description for this class
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
         * Create a new Feature with the given id.
         * <p>
         * TODO: This will also take in the feature's data.
         *
         * @return a new Feature with the given id
         */
        public Feature create();
    }

    /**
     * An enum that represents the different types of features that can be
     * spawned, along with their spawnrates.
     *
     * @author Andrew Wang
     * @version May 2024
     */
    public static enum Type {
        TREE(Tree.class, Tree::new, 1),
        CRATE(Crate.class, Crate::new, 2),
        TOMBSTONE(Tombstone.class, Tombstone::new, 1),
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
         * @return a new Feature of the given type
         */
        public static Feature createFeature(int i) {
            return values()[i].factory.create();
        }

        /**
         * Get the spawn rate of the given Feature type.
         *
         * @param i the index of the type
         * @return the spawn rate of the given type
         */
        public static int getSpawnRate(int i) {
            return spawnRates[i];
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

    /**
     * Create a new Feature with the given id and the given sheet name.
     * <p>
     * Refer to {@link Sprack#Sprack}
     * @param id the unique id used to identify this feature when regenerating the world
     */
    public Feature(String sheetName) {
        super(sheetName);
    }

    public void modify() {
        /**
         * ideally this should be part of an interface, but I'm not well versed
         * enough in interfaces to deal with this. This should modify the world
         * feature (such as having it destroyed)
         */

         // currently empty, as you can see...
    }
}
