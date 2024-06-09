import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * The different types of clusters that can be generated.
 * <p>
 * To add a new cluster of a certain type of {@link Feature}, add a new entry to
 * this enum with the following parameters:
 * <ul>
 * <li>The class of the {@link Feature} to generate</li>
 * <li>The spawn rate of the cluster</li>
 * <li>The density of the cluster</li>
 * <li>The maximum radius of the cluster (actually a square)</li>
 * </ul>
 *
 * @author Lucas Fu
 * @author Andrew Wang
 * @version May 2024
 */
public enum Cluster {
    OAK_TREE(OakTree.class, 6, 14, 12),
    WILLOW_TREE(WillowTree.class, 4, 12, 10),
    TOMBSTONE(Tombstone.class, 10, 60, 3),
    ;

    public final Class<? extends Feature> cls;
    public final int spawnRate;
    public final int density;
    public final int maxRadius;

    private static HashMap<Class<? extends Feature>, List<Vector2>> clusterCenters = new HashMap<>();

    private Cluster(Class<? extends Feature> cls, int spawnRate, int density, int maxRadius) {
        this.cls = cls;
        this.spawnRate = spawnRate;
        this.density = density;
        this.maxRadius = maxRadius;
    }

    static {
        for (Cluster cluster : values()) {
            clusterCenters.put(cluster.cls, new ArrayList<>());
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

    public static Cluster getFromFeature(Feature.Type type) {
        return valueOf(type.name());
    }

    public static List<Vector2> getCenter(Class<? extends Feature> cls) {
        return clusterCenters.get(cls);
    }

    public static void addCenter(Class<? extends Feature> cls, Vector2 center) {
        clusterCenters.get(cls).add(center);
    }

    public static void removeCenter(Class<? extends Feature> cls, Vector2 center) {
        clusterCenters.get(cls).remove(center);
    }

    public static void removeCenter(Vector2 center) {
        for (List<Vector2> centers : clusterCenters.values()) {
            centers.remove(center);
        }
    }

    public static void clearClusters() {
        for(List<Vector2> l : clusterCenters.values()) {
            l.clear();
        }
    }
}
