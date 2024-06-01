import java.util.HashMap;

/**
 * A class to store data for a feature.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public class FeatureData extends HashMap<String, Object> {
    public final long id;
    private Vector2 position;

    /**
     * Create a new FeatureData with the given id and position.
     *
     * @param id the id of this feature
     * @param position the position of this feature
     */
    public FeatureData(long id, Vector2 position) {
        super();
        this.id = id;
        this.position = position;
    }

    /**
     * Create a new FeatureData with the given id without initializing its
     * position.
     *
     * @param id the id of this feature
     */
    public FeatureData(long id) {
        this(id, new Vector2());
    }

    /**
     * Set the position of this feature.
     * <p>
     * This is used for features that have been loaded from a file, and the
     * position is not known at the time of loading, so needs to be set later
     * when the feature is actually spawned (i.e. location determined).
     *
     * @param position the new position of this feature
     */
    public void setPosition(Vector2 position) {
        this.position = position;
    }

    /**
     * Get the position of this feature.
     * <p>
     * This position is the position of the feature on the grid, not pixel
     * position.
     *
     * @return the position of this feature
     */
    public Vector2 getPosition() {
        return position;
    }

    /**
     * Create a string representation of this FeatureData that can be written
     * to the world save file.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(id);
        sb.append(",");
        for (String key : keySet()) {
            sb.append(key);
            sb.append(",");
            sb.append(get(key));
            sb.append(",");
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
