public class JesterSpawner extends EnemySpawner {
    public JesterSpawner(FeatureData data) {
        super(null, data, Jester::new, 1);
    }
}
