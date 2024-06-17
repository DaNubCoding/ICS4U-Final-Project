import greenfoot.Greenfoot;
/**
 * An invisible feature that spawns 3 statues in a triangle.
 */
public class StatueCampfire extends EnemySpawner {
    public StatueCampfire(FeatureData data) {
        super(null, data, Statue::new, 3);
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }
}
