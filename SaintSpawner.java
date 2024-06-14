public class SaintSpawner extends EnemySpawner {
    public SaintSpawner(FeatureData data) {
        super(null, data, Saint::new, 1);
    }
}
