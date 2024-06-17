import java.util.function.Supplier;

/**
 * An abstract class representing all features that spawn enemies.
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public abstract class EnemySpawner extends Feature {
    private Supplier<Enemy> spawner;
    private int spawnCount;

    /**
     * Create a new enemy spawner.
     * 
     * @param sheetname the sheet of the spawner
     * @param data data about the spawner
     * @param spawner the constructor of the enemy to spawn
     */
    public EnemySpawner(String sheetname, FeatureData data, Supplier<Enemy> spawner, int spawnCount) {
        super(sheetname, data);
        this.spawner = spawner;
        this.spawnCount = spawnCount;
    }

    /**
     * Get the enemy's constructor.
     * 
     * @return the enemy's constructor
     */
    public Supplier<Enemy> getSpawner() {
        return spawner;
    }

    /**
     * Get the number of enemies this spawner should spawn.
     * 
     * @return the number of enemies to spawn
     */
    public int getSpawnCount() {
        return spawnCount;
    }
}
