/**
 * Weapon that fires projectiles.
 *
 * @author Andrew Wang
 * @author Lucas Fu
 * @version May 2024
 */
public abstract class RangedWeapon extends Weapon {

    @FunctionalInterface
    public static interface ProjectileFactory {
        public Projectile create(Entity owner, Vector3 initialVel, Vector3 pos);
    }

    private ProjectileFactory projFactory;
    private int inaccuracy;
    private double speed;
    private int shotCount;

    /**
     * Create a new ranged weapon with all specifications.
     * 
     * @param image the file name of the image
     * @param inaccuracy the spread angle of the weapon
     * @param speed the projectile speed of projectiles shot
     * @param shotCount the number of projectiles shot each attack
     * @param windup the delay between input and the weapon firing
     * @param cooldown the delay between the weapon firing and registring the next input
     * @param projectileFactory the constructor of the projectile shot by this weapon
     */
    public RangedWeapon(String image, int inaccuracy,
                        double speed, int shotCount, int windup, int cooldown,
                        ProjectileFactory projectileFactory) {
        super(image, windup, cooldown);
        projFactory = projectileFactory;
        this.inaccuracy = inaccuracy;
        this.speed = speed;
        this.shotCount = shotCount;
    }

    /**
     * Get the player's shooting direction.
     *
     * @return the player's facing direction as a 3d vector with no y-componentS
     */
    private Vector3 getProjectileDirection() {
        return Vector3.fromXZ(new Vector2(getPlayer().getWorldRotation()));
    }

    @Override
    public void attack() {
        Vector3 spawnPos = getWorldPos();
        Vector3 direction = getProjectileDirection().multiply(speed);
        for(int i = 0; i < shotCount; i++) {
            Vector3 newDir = adjustForInaccuracy(direction, inaccuracy);
            Projectile proj = projFactory.create(getPlayer(), newDir, spawnPos);
            getPlayer().getWorld().addSprite(proj, 0, 0);
        }
    }

    /**
     * Modifies the initial velocity using inaccuracy.
     * 
     * @param initialVel the velocity before modifications
     * @param inaccuracy the inaccuracy modifier
     * @return the new modified velocity
     */
    private static Vector3 adjustForInaccuracy(Vector3 initialVel, int inaccuracy) {
        double dAngle = Math.random() * inaccuracy - inaccuracy / 2.0;
        double adjustedAngle = initialVel.xz.angle() + dAngle;
        Vector2 adjVector = new Vector2(adjustedAngle);
        return initialVel.add(Vector3.fromXZ(adjVector));
    }
}
