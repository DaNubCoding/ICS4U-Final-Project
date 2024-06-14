import java.util.HashMap;
import java.util.function.Supplier;

/**
 * An entity is a {@link Sprack} that can move and interact with the world. It
 * has certain levels of physics baked in, and a plethora of methods to help
 * with different movement patterns.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class Entity extends Sprack { // TODO: entity loading and unloading
    /**
     * The default acceleration due to the entity's internal forces.
     */
    public static final double MAX_ACCEL = 0.5;
    /**
     * The acceleration due to friction when the entity is on the ground.
     */
    public static final double FRIC_ACCEL = 0.2;
    /**
     * The acceleration due to air resistance when the entity is in the air.
     */
    public static final double AIR_RES_ACCEL = 0.03;
    /**
     * The angular acceleration of the entity when turning.
     */
    public static final double ROT_ACCEL = 0.2;
    /**
     * The default maximum speed at which the entity can move.
     */
    public static final double MAX_SPEED = 2.0;

    public final long id = Math.round(Math.random() * (Long.MAX_VALUE - 1));
    public final PhysicsController physics;
    private double health;

    public static final HashMap<String, Supplier<Entity>> NAMES = new HashMap<>();

    static {
        NAMES.put("statue", Statue::new);
        NAMES.put("jester", Jester::new);
        NAMES.put("saint", Saint::new);
    }

    /**
     * Create a new entity with the given sheet name and layer.
     * <p>
     * The sheet name is used to look up the {@link SprackView} object that
     * contains the pre-rendered images for this Entity.
     *
     * @param sheetName the name of the Sprack sheet
     * @param layer the layer to render the Entity on
     */
    public Entity(String sheetName, Layer layer) {
        super(sheetName, layer);
        physics = new PhysicsController(this);
        showShadow();
    }

    /**
     * Create a new Entity with the given looping animation of sheet names and
     * layer.
     * <p>
     * The sheet names are used to look up the {@link SprackView} objects that
     * contain the pre-rendered images for the animation.
     *
     * @param sheetAnimation an {@link Animation} object describing the looping
     *                       animation of SprackView names to assign to this
     *                       Entity
     * @param layer the layer to render the Entity on
     */
    public Entity(Animation sheetAnimation, Layer layer) {
        super(sheetAnimation, layer);
        physics = new PhysicsController(this);
        showShadow();
    }

    /**
     * Create a new Entity with the given fixed sheet name.
     * <p>
     * The sheet name is used to look up the {@link SprackView} object that
     * contains the pre-rendered images for this Entity.
     *
     * @param sheetName the name of the Sprack sheet
     */
    public Entity(String sheetName) {
        super(sheetName);
        physics = new PhysicsController(this);
        showShadow();
    }

    /**
     * Create a new Entity with the given looping animation of sheet names.
     * <p>
     * The sheet names are used to look up the {@link SprackView} objects that
     * contain the pre-rendered images for the animation.
     *
     * @param sheetAnimation an {@link Animation} object describing the looping
     *                       animation of SprackView names to assign to this
     *                       Entity
     */
    public Entity(Animation sheetAnimation) {
        super(sheetAnimation);
        physics = new PhysicsController(this);
        showShadow();
    }

    /**
     * Set the entity's health.
     *
     * @param health the health
     */
    public void setHealth(double health) {
        this.health = health;
    }

    /**
     * Get the entity's health.
     *
     * @return the health
     */
    public double getHealth() {
        return health;
    }

    /**
     * Damage the entity by the given amount of health.
     *
     * @param damage the amount of damage
     */
    public void damage(Damage damage) {
        setHealth(health - damage.getDamage());
        if (health <= 0) {
            if (damage.getOwner() instanceof Player)
                getWorld().getWorldData().addPlayerEnemiesKilled();
            health = 10000;
            die();
            return;
        }
        for (int i = 0; i < damage.getDamage() + 4; i++) {
            DamageParticle particle = new DamageParticle();
            Vector3 offset = new Vector3(
                Math.random() * 20 - 10,
                Math.random() * getHeight(),
                Math.random() * 20 - 10
            );
            getWorld().addWorldObject(particle, getWorldPos().add(offset));
        }
    }

    public void die() {
        for (int i = 0; i < 20; i++) {
            DeathParticle particle = new DeathParticle();
            Vector3 offset = new Vector3(
                Math.random() * 20 - 10,
                Math.random() * getHeight(),
                Math.random() * 20 - 10
            );
            getWorld().addWorldObject(particle, getWorldPos().add(offset));
        }
        getWorld().getWorldData().removeEntity(id);
        getWorld().removeSprite(this);
        getWorld().getWorldData().removeEntity(id);
    }

    @Override
    public String toString() {
        return getClass().getName().toLowerCase();
    }
}
