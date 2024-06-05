/**
 * An entity is a {@link Sprack} that can move and interact with the world. It
 * has certain levels of physics baked in, and a plethora of methods to help
 * with different movement patterns.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class Entity extends Sprack {
    /**
     * The acceleration due to the entity's internal forces.
     */
    public static final double ACCEL = 0.5;
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

    private Vector3 internalAccel;
    private Vector3 externalAccel;
    private Vector3 internalVel;
    private Vector3 externalVel;
    private double maxSpeed;

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
        init();
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
        init();
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
        init();
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
        init();
    }

    private void init() {
        internalAccel = new Vector3();
        externalAccel = new Vector3();
        internalVel = new Vector3();
        externalVel = new Vector3();
        maxSpeed = MAX_SPEED;
    }

    /**
     * Accelerate the entity in the direction of the given {@link Vector3}.
     * <p>
     * This is an internally generated acceleration that respects the entity's
     * defined maximum speed.
     *
     * @param accel the acceleration vector
     */
    public void accelerate(Vector3 accel) {
        internalAccel = internalAccel.add(accel);
    }

    /**
     * Accelerate the entity in the direction of the given {@link Vector2} on
     * the XZ plane.
     * <p>
     * This is an internally generated acceleration that respects the entity's
     * defined maximum speed.
     *
     * @param accel the acceleration vector
     */
    public void accelerate(Vector2 accel) {
        accelerate(Vector3.fromXZ(accel));
    }

    /**
     * Accelerate the entity towards the given {@link Vector3}.
     * <p>
     * This is an internally generated acceleration that respects the entity's
     * defined maximum speed.
     *
     * @param target the target position
     */
    public void accelTowards(Vector3 target) {
        accelerate(target.subtract(getWorldPos()));
    }

    /**
     * Accelerate the entity towards the given {@link Vector2} on the XZ plane.
     * <p>
     * This is an internally generated acceleration that respects the entity's
     * defined maximum speed.
     *
     * @param target the target position
     */
    public void accelTowards(Vector2 target) {
        accelTowards(Vector3.fromXZ(target));
    }

    /**
     * Accelerate the entity towards the player.
     * <p>
     * This is an internally generated acceleration that respects the entity's
     * defined maximum speed.
     */
    public void accelTowardsPlayer() {
        Player player = getWorld().getPlayer();
        accelTowards(player.getWorldPos());
    }

    /**
     * Apply a 3D impulse to the entity as a {@link Vector3}.
     * <p>
     * This is an externally applied force that does not respect the entity's
     * defined maximum speed.
     *
     * @param impulse the impulse vector
     */
    public void applyImpulse(Vector3 impulse) {
        externalVel = externalVel.add(impulse);
    }

    /**
     * Apply a 2D impulse to the entity as a {@link Vector2} on the XZ plane.
     * <p>
     * This is an externally applied force that does not respect the entity's
     * defined maximum speed.
     *
     * @param impulse the impulse vector
     */
    public void applyImpulse(Vector2 impulse) {
        applyImpulse(Vector3.fromXZ(impulse));
    }

    /**
     * Apply a force to the entity as a {@link Vector3}.
     * <p>
     * This is an externally applied force that does not respect the entity's
     * defined maximum speed.
     *
     * @param force the force vector
     */
    public void applyForce(Vector3 force) {
        externalAccel = externalAccel.add(force);
    }

    /**
     * Apply a force to the entity as a {@link Vector2} on the XZ plane.
     * <p>
     * This is an externally applied force that does not respect the entity's
     * defined maximum speed.
     *
     * @param force the force vector
     */
    public void applyForce(Vector2 force) {
        applyForce(Vector3.fromXZ(force));
    }

    /**
     * Reduce the entity's horizontal momentum by the given factor.
     * <p>
     * A factor of
     *
     * @param factor the factor by which to reduce the entity's momentum
     */
    public void reduceMomentum(double factor) {
        internalVel = internalVel.multiply(1 - factor);
        externalVel = externalVel.multiply(1 - factor);
    }

    /**
     * Update the entity's position based on its acceleration and velocity.
     */
    public void updateMovement() {
        // Clamp internal acceleration
        try {
            internalAccel = internalAccel.scaleToMagnitude(ACCEL);
        } catch (ArithmeticException e) {} // Do nothing if acceleration is zero

        // Determine whether to use friction or air resistance
        final double fricAccel = getWorldY() == 0 ? FRIC_ACCEL : AIR_RES_ACCEL;

        // Apply acceleration due to friction to internal velocity
        try {
            double horFricMag = Math.min(internalVel.xz.magnitude(), fricAccel);
            Vector2 fric = internalVel.xz.scaleToMagnitude(horFricMag);
            internalAccel = internalAccel.subtractXZ(fric);
        } catch (ArithmeticException e) {} // Do nothing if velocity is zero

        // Apply acceleration due to friction to external velocity
        try {
            double horFricMag = Math.min(externalVel.xz.magnitude(), fricAccel);
            Vector2 fric = externalVel.xz.scaleToMagnitude(horFricMag);
            externalAccel = externalAccel.subtractXZ(fric);
        } catch (ArithmeticException e) {} // Do nothing if velocity is zero

        // Update internal velocity
        internalVel = internalVel.add(internalAccel);

        // Clamp internal velocity
        internalVel = internalVel.clampMagnitude(maxSpeed);

        // Update external velocity
        externalVel = externalVel.add(externalAccel);

        // Update position
        setWorldPos(getWorldPos().add(getVelocity()));

        // Clamp position
        if (getWorldY() < 0) {
            setWorldPos(getWorldPos().setY(0));
            internalVel = internalVel.setY(0);
            externalVel = externalVel.setY(0);
        }

        // Reset acceleration
        internalAccel = new Vector3();
        externalAccel = new Vector3();
    }

    /**
     * Turn the entity towards the direction it is moving.
     */
    public void turnTowardsMovement() {
        if (!isMoving()) return;
        double facing = Vector2.lerpAngle(
            getWorldRotation(),
            internalVel.xz.angle(),
            ROT_ACCEL
        );
        setWorldRotation(facing);
    }

    /**
     * Get the entity's cumulative velocity.
     * <p>
     * This is the sum of the entity's internal and external velocities.
     *
     * @return the entity's velocity
     */
    public Vector3 getVelocity() {
        return internalVel.add(externalVel);
    }

    /**
     * Get the entity's internal velocity.
     * <p>
     * This is the velocity caused by the entity's internal acceleration, which
     * does not include the velocity caused by external forces.
     *
     * @return the entity's internal velocity
     */
    public Vector3 getInternalVelocity() {
        return internalVel;
    }

    /**
     * Get the entity's external velocity.
     * <p>
     * This is the velocity caused by external forces, which does not include
     * the velocity caused by the entity's internal acceleration.
     *
     * @return the entity's external velocity
     */
    public Vector3 getExternalVelocity() {
        return externalVel;
    }

    /**
     * Get whether the entity is moving.
     * <p>
     * This is determined by the entity's internal velocity, this does not
     * include the velocity due to external forces.
     *
     * @return true if the entity is moving, false otherwise
     */
    public boolean isMoving() {
        return internalVel.xz.magnitude() != 0;
    }

    /**
     * Set the entity's maximum speed.
     * <p>
     * Note that this will only affect the maximum speed at which the entity
     * will move on its own accord. External forces can still cause the entity
     * to move faster than this speed.
     *
     * @param maxSpeed the maximum speed
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Get the entity's maximum speed.
     * <p>
     * Note that this is the maximum speed at which the entity will move on its
     * own accord. External forces can still cause the entity to move faster
     * than this speed.
     *
     * @return the maximum speed
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }
}
