/**
 * A... grave?
 *
 * @author Andrew Wang
 * @author Sandra Huang
 * @version May 2024
 */
public class Tombstone extends EnemySpawner {
    /**
     * Create a new Tombstone.
     */
    public Tombstone(FeatureData data) {
        super("tombstone", data, Skeleton::new, 1);
    }
}
