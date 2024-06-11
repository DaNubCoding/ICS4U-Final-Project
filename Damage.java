/**
 * A class that represents a damage source. It is a spherical area of effect
 * inside which entities can take damage.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class Damage {
    private final Entity owner;
    private final Sprite source;
    private Entity target;
    private final double damage;
    private final Vector3 center;
    private final int radius;
    private double minAngle;
    private double maxAngle;
    private int lifetime;
    private int interval;

    private boolean removed;
    private Timer lifeTimer;
    private Timer damageTimer;

    /**
     * Create a new damage source.
     *
     * @param owner the owner of the damage (see {@link #getOwner()})
     * @param source the source of the damage (see {@link #getSource()})
     * @param damage the amount of damage that this damage source deals (see
     *               {@link #getDamage()})
     * @param center the center of the damage source (see {@link #getCenter()})
     * @param radius the radius of the damage source (see {@link #getRadius()})
     */
    public Damage(Entity owner, Sprite source, double damage, Vector3 center, int radius) {
        this.owner = owner;
        this.source = source;
        this.damage = damage;
        this.center = center;
        this.radius = radius;

        lifeTimer = new Timer(0);
        damageTimer = new Timer(0);
    }

    /**
     * Set the target of the damage. If it is not null, this is the entity that
     * will be considered for taking damage and no other entity will be affected
     * by this damage source.
     *
     * @param target the target of the damage
     */
    public void setTarget(Entity target) {
        this.target = target;
    }

    /**
     * Set the minimum angle of the damage source.
     * <p>
     * Consider a sector on a unit circle, the angle of the one of the arms of
     * the sector is this angle. The region within this sector is the area of
     * effect where entities can take damage. The sector is constructed by going
     * in the positive direction from the minimum angle to the maximum angle.
     *
     * @param minAngle the minimum angle of the damage source
     */
    public void setMinAngle(double minAngle) {
        this.minAngle = minAngle;
    }

    /**
     * Set the maximum angle of the damage source.
     * <p>
     * Consider a sector on a unit circle, the angle of the one of the arms of
     * the sector is this angle. The region within this sector is the area of
     * effect where entities can take damage. The sector is constructed by going
     * in the positive direction from the minimum angle to the maximum angle.
     *
     * @param maxAngle the maximum angle of the damage source
     */
    public void setMaxAngle(double maxAngle) {
        this.maxAngle = maxAngle;
    }

    /**
     * Set the angular range of the damage source. This defines the center of
     * the sector and the angular range of the sector in which entities can take
     * damage.
     * <p>
     * By default, the sector is a full circle.
     *
     * @param centerAngle the center angle of the sector
     * @param range the range of the sector in degrees
     */
    public void setAngularRange(double centerAngle, int range) {
        minAngle = centerAngle - range / 2;
        maxAngle = centerAngle + range / 2;
    }

    /**
     * Set the lifetime of the damage source. This is the number of frames that
     * the damage source will last for before being removed.
     * <p>
     * By default, the lifetime is one frame.
     *
     * @param lifetime the lifetime of the damage source
     */
    public void setLifetime(int lifetime) {
        this.lifetime = lifetime;
        lifeTimer = new Timer(lifetime);
    }

    /**
     * Set the interval between each time the damage source deals damage. This
     * is the number of frames between each time the damage source deals damage.
     * <p>
     * By default, the damage source will deal damage every frame.
     *
     * @param interval the interval between each time the damage source deals
     *                 damage
     */
    public void setInterval(int interval) {
        this.interval = interval;
        damageTimer = new Timer(interval);
    }

    /**
     * Update the damage source. This will deal damage to entities within the
     * area of effect.
     */
    public void update() {
        if (damageTimer.ended()) {
            SprackWorld world = owner.getWorld();
            // If the owner is removed from the world, remove the damage
            if (world == null) {
                removed = true;
                return;
            }
            for (Entity entity : world.getEntitiesInRange(center, radius)) {
                if (entity == owner) continue;
                if (target != null && entity != target) continue;
                if (minAngle == 0 && maxAngle == 0) {
                    entity.damage(this);
                    continue;
                }
                double angle = entity.getWorldPos().subtract(center).xz.angle();
                double angle2 = angle + 360;
                double angle3 = angle - 360;
                if (angle > minAngle && angle < maxAngle
                    || angle2 > minAngle && angle2 < maxAngle
                    || angle3 > minAngle && angle3 < maxAngle) {
                    entity.damage(this);
                }
            }
            damageTimer.restart();
        }

        if (lifeTimer.ended()) {
            removed = true;
        }
    }

    /**
     * Returns a new damage source that is a copy of this damage source but with
     * the damage multiplied by a scalar.
     *
     * @param scalar the scalar to multiply the damage by
     * @return a new damage source with the damage multiplied by the scalar
     */
    public Damage multiply(double scalar) {
        return new Damage(owner, source, damage * scalar, center, radius);
    }

    /**
     * Check if the damage has been removed from the world.
     * <p>
     * This is only used by the world to determine if the damage should be
     * removed from the list of damages after iteration.
     *
     * @return true if the damage has been removed, false otherwise
     */
    public boolean isRemoved() {
        return removed;
    }

    /**
     * Get the owner of the damage. This will always be the entity that caused
     * the damage, like the player or an enemy. In short, it is the root cause
     * of the damage.
     *
     * @return the owner of the damage
     */
    public Entity getOwner() {
        return owner;
    }

    /**
     * Get the source of the damage. If the attack was melee, this would likely
     * be the same as the owner ({@link #getOwner()}). If the attack was ranged,
     * this would most likely be the projectile that caused the damage. In
     * short, it is the most direct cause of the damage.
     * <p>
     * This is useful for determining the source of the damage, like having
     * different behavior based on the type of projectile that caused the
     * damage.
     *
     * @return the source of the damage
     */
    public Sprite getSource() {
        return source;
    }

    /**
     * Get the target of the damage. If it is not null, this is the entity that
     * will be considered for taking damage and no other entity will be affected
     * by this damage source.
     *
     * @return the target of the damage
     */
    public Entity getTarget() {
        return target;
    }

    /**
     * Get the amount of damage that this damage source deals.
     *
     * @return the damage amount
     */
    public double getDamage() {
        return damage;
    }

    /**
     * Get the center of the damage source. This is the center of the area of
     * effect where entities can take damage.
     *
     * @return the center of the damage source
     */
    public Vector3 getCenter() {
        return center;
    }

    /**
     * Get the radius of the damage source. This is the radius of the area of
     * effect where entities can take damage.
     *
     * @return the radius of the damage source
     */
    public int getRadius() {
        return radius;
    }

    /**
     * Get the minimum angle of the damage source.
     * <p>
     * Consider a sector on a unit circle, the angle of the one of the arms of
     * the sector is this angle. The region within this sector is the area of
     * effect where entities can take damage. The sector is constructed by going
     * in the positive direction from the minimum angle to the maximum angle.
     *
     * @return the minimum angle of the damage source
     */
    public double getMinAngle() {
        return minAngle;
    }

    /**
     * Get the maximum angle of the damage source.
     * <p>
     * Consider a sector on a unit circle, the angle of the one of the arms of
     * the sector is this angle. The region within this sector is the area of
     * effect where entities can take damage. The sector is constructed by going
     * in the positive direction from the minimum angle to the maximum angle.
     *
     * @return the maximum angle of the damage source
     */
    public double getMaxAngle() {
        return maxAngle;
    }

    /**
     * Get the lifetime of the damage source. This is the number of frames that
     * the damage source will last for before being removed.
     *
     * @return the lifetime of the damage source
     */
    public int getLifetime() {
        return lifetime;
    }

    /**
     * Get the interval of the damage source. This is the number of frames
     * between each time the damage source deals damage.
     *
     * @return the interval of the damage source
     */
    public int getInterval() {
        return interval;
    }
}
