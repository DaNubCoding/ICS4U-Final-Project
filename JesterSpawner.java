/**
 * An invisible feature that spawns a single Jester.
 */
public class JesterSpawner extends EnemySpawner {
    public JesterSpawner(FeatureData data) {
        super(null, data, Jester::new, 1);
    }
}
