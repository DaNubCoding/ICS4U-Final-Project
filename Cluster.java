import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

/**
 * The different types of clusters that can be generated.
 *
 * @author Lucas Fu
 * @author Andrew Wang
 * @version May 2024
 */
public enum Cluster {
    TREE(Tree.class, 1, 10, 8),
    TOMBSTONE(Tombstone.class, 3, 40, 3),
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
}
