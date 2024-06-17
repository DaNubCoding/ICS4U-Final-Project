/**
 * An invisible feature that spawns a Saint.
 * 
 * @author Stanley
 * @version June 2024
 */
public class SaintSpawner extends EnemySpawner {
    public SaintSpawner(FeatureData data) {
        super(null, data, Saint::new, 1);
    }
}
