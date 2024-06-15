/**
 * A... grave?
 *
 * @author Andrew Wang
 * @author Sandra Huang
 * @version May 2024
 */
public class Tombstone extends Feature {
    /**
     * Create a new Tombstone.
     */
    public Tombstone(FeatureData data) {
        super("tombstone", data);
    }

    public void update() {
        if (getWorldPos().xz.magnitude() < 500) return;

        if (getWorld().getPlayer().getWorldPos().distanceTo(getWorldPos()) < 80
        && !getData().containsKey("spawnedEnemies")) {
            getData().put("spawnedEnemies", null);
            Enemy toBeSpawned;
            if (Math.random() < 0.8) toBeSpawned = new Skeleton();
            else toBeSpawned = new Ghost();
            getWorld().addWorldObject(toBeSpawned, getWorldPos());
        }
    }
}
