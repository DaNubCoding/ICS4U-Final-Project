import greenfoot.*;

/**
 * The player controlled by the user.
 *
 * @author Martin Baldwin
 * @author Andrew Wang
 * @version May 2024
 */
public class Player extends Entity {
    private static final Animation walkingAnimation = new Animation(6,
        "knight_walk1",
        "knight_walk2",
        "knight_walk3",
        "knight_walk4"
    );
    private static final Animation standingAnimation = new Animation(-1, "knight_standing");

    private double cameraTargetRotation;
    private Timer dashTimer;

    private Weapon weapon;

    public Player() {
        super(standingAnimation);
        dashTimer = new Timer(90);
        weapon = new TestSword(this);
    }

    @Override
    public void update() {
        // Apply input acceleration
        if (Greenfoot.isKeyDown("d")) {
            accelerate(new Vector2(Camera.getRotation()));
        }
        if (Greenfoot.isKeyDown("s")) {
            accelerate(new Vector2(Camera.getRotation() + 90));
        }
        if (Greenfoot.isKeyDown("a")) {
            accelerate(new Vector2(Camera.getRotation() + 180));
        }
        if (Greenfoot.isKeyDown("w")) {
            accelerate(new Vector2(Camera.getRotation() + 270));
        }

        // Dashing
        if (Greenfoot.isKeyDown("space") && dashTimer.ended()) {
            applyImpulse(new Vector2(getWorldRotation()).multiply(6));
            dashTimer.restart();
        }

        // TODO: TEMPORARY for demo purposes
        if (Greenfoot.isKeyDown("q") && getWorldY() == 0) {
            reduceMomentum(0.33);
            applyImpulse(new Vector3(0, 4, 0));
        }

        // TODO: TEMPORARY for demo purposes
        if (Greenfoot.isKeyDown("e") && getWorldY() == 0) {
            reduceMomentum(0.9);
            Vector2 horImpulse = new Vector2(getWorldRotation()).multiply(5);
            applyImpulse(Vector3.fromXZ(horImpulse).add(new Vector3(0, 6, 0)));
        }

        // TODO: move to Entity after demo
        // Apply gravitational force
        applyForce(new Vector3(0, -0.2, 0));

        // Update position with velocity and friction, and update position
        updateMovement();

        // Turn towards where the player is moving
        turnTowardsMovement();

        // Set the animation based on whether the player is moving
        if (isMoving()) {
            setLoopingAnimation(walkingAnimation);
        } else {
            setLoopingAnimation(standingAnimation);
        }

        // Update camera stuff
        Camera.targetPosition(getWorldPos().add(new Vector3(0, 10, 0)));
        updateCameraRotation();

        // Update player's weapon
        weapon.update();
    }

    @Override
    public void render(GreenfootImage canvas) {
        double rot = getVisualRotation();
        // Render the weapon behind the player if the player is facing away
        if ((rot >= 0 && rot < 180) || rot > 330) {
            super.render(canvas);
            weapon.render(canvas);
        } else {
            weapon.render(canvas);
            super.render(canvas);
        }
    }

    private void updateCameraRotation() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null && mouse.getButton() == 1) {
            if (Greenfoot.mousePressed(null)) {
                MouseManager.initMouseLock();
            }
        }
        if (Greenfoot.mouseClicked(null)) {
            MouseManager.releaseMouseLock();
        }
        if (!MouseManager.isLocked()) return;
        Vector2 mouseRel = MouseManager.getMouseRel();
        MouseManager.lockMouse();
        cameraTargetRotation += mouseRel.x * 0.13;
        double zoom = Camera.getZoom() * (1 + mouseRel.y * 0.002);
        Camera.setZoom(Math.max(0.8, Math.min(6, zoom)));
        Camera.targetRotation(cameraTargetRotation);
    }
}
