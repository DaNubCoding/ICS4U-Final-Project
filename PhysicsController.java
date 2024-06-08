/**
 * A controller for the physics of a {@link WorldObject}. This is used to apply
 * certain physics forces or rules to a receiving client object.
 * <p>
 * To add physics to a client object, create a new {@link PhysicsController}
 * with the client object as the host, and call {@link #update()} on the
 * controller every frame. Apply forces to the client before calling update.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class PhysicsController {
    /**
     * The default magnitude of the acceleration due to the client's internal
     * forces.
     */
    public static final double MAX_ACCEL_MAG = 0.5;
    /**
     * The magnitude of the acceleration due to friction when the client is on
     * the ground.
     */
    public static final double FRIC_MAG = 0.2;
    /**
     * The default acceleration due to air resistance.
     */
    public static final double AIR_RES_MAG = 0.03;
    /**
     * The angular acceleration of the client when turning.
     */
    public static final double ROT_ACCEL = 0.2;
    /**
     * The default maximum speed at which the client can move.
     */
    public static final double MAX_SPEED = 2.0;
    /**
     * The magnitude of the acceleration due to gravity.
     */
    public static final double GRAVITY = -0.2;

    private WorldObject client;

    private Vector3 internalAccel;
    private Vector3 externalAccel;
    private Vector3 internalVel;
    private Vector3 externalVel;
    private double maxSpeed;
    private double maxAccel;
    private double airResMag;
    private Vector3 target;
    private boolean targeting;
    private boolean alwaysTurnTowardsMovement;
    private boolean affectedByGravity;
    private boolean affectedByFrictionalForces;

    public PhysicsController(WorldObject client) {
        this.client = client;
        internalAccel = new Vector3();
        externalAccel = new Vector3();
        internalVel = new Vector3();
        externalVel = new Vector3();
        maxSpeed = MAX_SPEED;
        maxAccel = MAX_ACCEL_MAG;
        airResMag = AIR_RES_MAG;
        affectedByGravity = true;
        affectedByFrictionalForces = true;
    }

    /**
     * Get the world position of the client of this controller.
     *
     * @return the world position
     */
    public Vector3 getWorldPos() {
        return client.getWorldPos();
    }

    /**
     * Get the world the client of this controller is in. This is always a
     * {@link SpriteStackingWorld}.
     *
     * @return the world
     */
    public SpriteStackingWorld getWorld() {
        return client.getWorld();
    }

    /**
     * Get the x position of the client of this controller in the world.
     *
     * @return the world x position
     */
    public double getWorldX() {
        return client.getWorldX();
    }

    /**
     * Get the y position of the client of this controller in the world.
     *
     * @return the world y position
     */
    public double getWorldY() {
        return client.getWorldY();
    }

    /**
     * Get the z position of the client of this controller in the world.
     *
     * @return the world z position
     */
    public double getWorldZ() {
        return client.getWorldZ();
    }

    /**
     * Set the world position of the client of this controller.
     *
     * @param position the world position
     */
    public void setWorldPos(Vector3 position) {
        client.setWorldPos(position);
    }

    /**
     * Get the world rotation of the client of this controller.
     *
     * @return the world rotation
     */
    public double getWorldRotation() {
        return client.getWorldRotation();
    }

    /**
     * Set the world rotation of the client of this controller.
     *
     * @param rotation the world rotation
     */
    public void setWorldRotation(double rotation) {
        client.setWorldRotation(rotation);
    }

    /**
     * Accelerate the client in the direction of the given {@link Vector3}.
     * <p>
     * This is an internally generated acceleration that respects the defined
     * maximum speed.
     *
     * @param accel the acceleration vector
     */
    public void accelerate(Vector3 accel) {
        internalAccel = internalAccel.add(accel);
    }

    /**
     * Accelerate the client in the direction of the given {@link Vector2} on
     * the XZ plane.
     * <p>
     * This is an internally generated acceleration that respects the defined
     * maximum speed.
     *
     * @param accel the acceleration vector
     */
    public void accelerate(Vector2 accel) {
        accelerate(Vector3.fromXZ(accel));
    }

    /**
     * Accelerate the client towards the given {@link Vector3}.
     * <p>
     * This is an internally generated acceleration that respects the defined
     * maximum speed.
     *
     * @param target the target position
     */
    public void accelTowards(Vector3 target) {
        accelerate(target.subtract(getWorldPos()));
    }

    /**
     * Accelerate the client towards the given {@link Vector2} on the XZ plane.
     * <p>
     * This is an internally generated acceleration that respects the defined
     * maximum speed.
     *
     * @param target the target position
     */
    public void accelTowards(Vector2 target) {
        accelTowards(Vector3.fromXZ(target));
    }

    /**
     * Schedule the client to move to the given {@link Vector3}.
     *
     * @param target the target position
     */
    public void moveToTarget(Vector3 target) {
        this.target = target;
        targeting = true;
    }

    /**
     * Schedule the client to move to the given {@link Vector2} on the XZ plane.
     *
     * @param target the target position
     */
    public void moveToTarget(Vector2 target) {
        moveToTarget(Vector3.fromXZ(target));
    }

    /**
     * Schedule the client to move to the closest point around the player within
     * the given range.
     *
     * @param range the range around the player to move to
     */
    public void moveToNearPlayer(double range) {
        Vector3 playerPos = getWorld().getPlayer().getWorldPos();
        if (playerPos.distanceTo(getWorldPos()) <= range) return;
        Vector3 delta = getWorldPos().subtract(playerPos);
        moveToTarget(playerPos.add(delta.scaleToMagnitude(range)));
    }

    /**
     * Forget the target position and stop moving towards it.
     */
    public void forgetTarget() {
        targeting = false;
    }

    /**
     * Apply a 3D impulse to the client as a {@link Vector3}.
     * <p>
     * This is an externally applied force that does not respect the defined
     * maximum speed.
     *
     * @param impulse the impulse vector
     */
    public void applyImpulse(Vector3 impulse) {
        externalVel = externalVel.add(impulse);
    }

    /**
     * Apply a 2D impulse to the client as a {@link Vector2} on the XZ plane.
     * <p>
     * This is an externally applied force that does not respect the defined
     * maximum speed.
     *
     * @param impulse the impulse vector
     */
    public void applyImpulse(Vector2 impulse) {
        applyImpulse(Vector3.fromXZ(impulse));
    }

    /**
     * Apply a force to the client as a {@link Vector3}.
     * <p>
     * This is an externally applied force that does not respect the defined
     * maximum speed.
     *
     * @param force the force vector
     */
    public void applyForce(Vector3 force) {
        externalAccel = externalAccel.add(force);
    }

    /**
     * Apply a force to the client as a {@link Vector2} on the XZ plane.
     * <p>
     * This is an externally applied force that does not respect the defined
     * maximum speed.
     *
     * @param force the force vector
     */
    public void applyForce(Vector2 force) {
        applyForce(Vector3.fromXZ(force));
    }

    /**
     * Reduce the client's horizontal momentum by the given factor.
     * <p>
     * A factor of 1 will completely remove the client's momentum, while a
     * factor of 0 will leave the client's momentum unchanged.
     *
     * @param factor the factor by which to reduce the client's momentum
     */
    public void reduceMomentum(double factor) {
        internalVel = internalVel.multiply(1 - factor);
        externalVel = externalVel.multiply(1 - factor);
    }

    /**
     * Update the rest of the client's movement, applying forces previously
     * applied.
     */
    public void update() {
        if (targeting) {
            accelTowards(target);
            // Vf^2 = Vi^2 + 2ad
            // d = (Vf^2 - Vi^2) / 2a
            // Vf = 0
            // d = -Vi^2 / 2a
            final double vi = internalVel.xz.magnitude();
            final double d = vi * vi / (2 * FRIC_MAG);
            // stop accelerating if the client is close enough to the target
            // that friction will stop it just in time to reach the target
            if (getWorldPos().distanceTo(target) < d) {
                targeting = false;
            }
        }

        // Clamp internal acceleration
        try {
            internalAccel = internalAccel.clampMagnitude(maxAccel);
        } catch (ArithmeticException e) {} // Do nothing if acceleration is zero

        // Apply gravity
        if (affectedByGravity) applyForce(new Vector3(0, GRAVITY, 0));

        if (affectedByFrictionalForces) {
            // Apply acceleration due to air resistance to internal velocity
            try {
                double mag = Math.min(internalVel.magnitude(), airResMag);
                Vector3 airRes = internalVel.scaleToMagnitude(mag);
                internalVel = internalVel.subtract(airRes);
            } catch (ArithmeticException e) {} // Do nothing if velocity is zero

            if (getWorldY() == 0) {
                // Apply acceleration due to friction to internal velocity
                try {
                    double horFricMag = Math.min(internalVel.xz.magnitude(), FRIC_MAG);
                    Vector2 fric = internalVel.xz.scaleToMagnitude(horFricMag);
                    internalVel = internalVel.subtractXZ(fric);
                } catch (ArithmeticException e) {} // Do nothing if velocity is zero
            }
        }

        // Update internal velocity
        internalVel = internalVel.add(internalAccel);

        // Clamp internal velocity
        internalVel = internalVel.clampMagnitude(maxSpeed);

        if (affectedByFrictionalForces) {
            // Apply acceleration due to air resistance to external velocity
            try {
                double mag = Math.min(externalVel.magnitude(), airResMag);
                Vector3 airRes = externalVel.scaleToMagnitude(mag);
                externalVel = externalVel.subtract(airRes);
            } catch (ArithmeticException e) {} // Do nothing if velocity is zero

            if (getWorldY() == 0) {
                // Apply acceleration due to friction to external velocity
                try {
                    double horFricMag = Math.min(externalVel.xz.magnitude(), FRIC_MAG);
                    Vector2 fric = externalVel.xz.scaleToMagnitude(horFricMag);
                    externalVel = externalVel.subtractXZ(fric);
                } catch (ArithmeticException e) {} // Do nothing if velocity is zero
            }
        }

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

        // Turn towards movement
        if (alwaysTurnTowardsMovement) {
            turnTowardsMovement();
        }
    }

    /**
     * Turn the client towards the direction it is moving.
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
     * Set whether the client should always turn towards the direction it is
     * moving.
     *
     * @param alwaysTurnTowardsMovement whether the client should always turn
     *                                  towards movement
     */
    public void setAlwaysTurnTowardsMovement(boolean alwaysTurnTowardsMovement) {
        this.alwaysTurnTowardsMovement = alwaysTurnTowardsMovement;
    }

    /**
     * Set whether the client is affected by gravity.
     *
     * @param affectedByGravity whether the client is affected by gravity
     */
    public void setAffectedByGravity(boolean affectedByGravity) {
        this.affectedByGravity = affectedByGravity;
    }

    /**
     * Set whether the client is affected by frictional forces. This includes
     * ground friction and air resistance.
     *
     * @param affectedByFrictionalForces whether the client is affected by
     *                                   frictional forces
     */
    public void setAffectedByFrictionalForces(boolean affectedByFrictionalForces) {
        this.affectedByFrictionalForces = affectedByFrictionalForces;
    }

    /**
     * Get the client's cumulative velocity.
     * <p>
     * This is the sum of the client's internal and external velocities.
     *
     * @return the client's velocity
     */
    public Vector3 getVelocity() {
        return internalVel.add(externalVel);
    }

    /**
     * Get the client's internal velocity.
     * <p>
     * This is the velocity caused by the client's internal acceleration, which
     * does not include the velocity caused by external forces.
     *
     * @return the client's internal velocity
     */
    public Vector3 getInternalVelocity() {
        return internalVel;
    }

    /**
     * Get the client's external velocity.
     * <p>
     * This is the velocity caused by external forces, which does not include
     * the velocity caused by the client's internal acceleration.
     *
     * @return the client's external velocity
     */
    public Vector3 getExternalVelocity() {
        return externalVel;
    }

    /**
     * Get whether the client is moving.
     * <p>
     * This is determined by the client's internal velocity, this does not
     * include the velocity due to external forces.
     *
     * @return true if the client is moving, false otherwise
     */
    public boolean isMoving() {
        return internalVel.xz.magnitude() != 0;
    }

    /**
     * Set the client's maximum speed.
     * <p>
     * Note that this will only affect the maximum speed at which the client
     * will move on its own accord. External forces can still cause the client
     * to move faster than this speed.
     *
     * @param maxSpeed the maximum speed
     */
    public void setMaxSpeed(double maxSpeed) {
        this.maxSpeed = maxSpeed;
    }

    /**
     * Set the client's maximum acceleration.
     * <p>
     * This value must be greater than the frictional acceleration to have any
     * effect when the client is on the ground.
     *
     * @param maxAccel the maximum acceleration
     */
    public void setMaxAccelMag(double maxAccel) {
        this.maxAccel = maxAccel;
    }

    /**
     * Set the magnitude of the acceleration due to air resistance.
     *
     * @param airResMag the magnitude of the acceleration due to air resistance
     */
    public void setAirResistanceMagnitude(double airResMag) {
        this.airResMag = airResMag;
    }

    /**
     * Get the client's maximum speed.
     * <p>
     * Note that this is the maximum speed at which the client will move on its
     * own accord. External forces can still cause the client to move faster
     * than this speed.
     *
     * @return the maximum speed
     */
    public double getMaxSpeed() {
        return maxSpeed;
    }
}
