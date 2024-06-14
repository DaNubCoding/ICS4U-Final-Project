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

    public void update() {
        if (getWorld().getPlayer().getWorldPos().distanceTo(getWorldPos()) < 80
        && !getData().containsKey("spawnedEnemies")) {
            getData().put("spawnedEnemies", null);
            getWorld().addWorldObject(new Skeleton(), getWorldPos().subtract(new Vector3(0, 10, 0)));
        }
    }
}
