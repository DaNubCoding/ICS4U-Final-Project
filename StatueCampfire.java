import greenfoot.Greenfoot;

public class StatueCampfire extends EnemySpawner {
    public StatueCampfire(FeatureData data) {
        super("tree_willow_canopy", data, Statue::new, 3);
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        getWorld().addCollisionController(new CollisionController(this, 10, 1.0, 0.0));
    }
}
