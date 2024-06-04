import greenfoot.*;

/**
 * The player controlled by the user.
 *
 * @author Martin Baldwin
 * @author Andrew Wang
 * @version May 2024
 */
public class Player extends Sprack {
    private static final double ACCEL = 0.5;
    private static final double FRIC_ACCEL = 0.2;
    private static final double MAX_SPEED = 3.0;

    private static final double ROT_ACCEL = 0.2;

    private static final Animation walkingAnimation = new Animation(6,
        "knight_walk1",
        "knight_walk2",
        "knight_walk3",
        "knight_walk4"
    );
    private static final Animation standingAnimation = new Animation(-1, "knight_standing");

    private Vector2 velocity;
    private double facing;
    private double cameraTargetRotation;

    private Weapon weapon;

    public Player() {
        super(standingAnimation);
        velocity = new Vector2(0, 0);
        facing = 0;

        weapon = new WandOfManyCrates(this);
    }

    @Override
    public void update() {
        updateCameraRotation();

        // Get input acceleration
        Vector2 accel = new Vector2(0, 0);
        if (Greenfoot.isKeyDown("w")) {
            accel = accel.add(new Vector2(0, -1));
        }
        if (Greenfoot.isKeyDown("s")) {
            accel = accel.add(new Vector2(0, 1));
        }
        if (Greenfoot.isKeyDown("a")) {
            accel = accel.add(new Vector2(-1, 0));
        }
        if (Greenfoot.isKeyDown("d")) {
            accel = accel.add(new Vector2(1, 0));
        }
        try {
            // Normalize diagonal movement
            accel = accel.normalize().multiply(ACCEL);
        } catch (ArithmeticException e) {
            accel = new Vector2(0, 0);
        }
        accel = accel.rotate(Camera.getRotation());

        velocity = velocity.add(accel);

        // Apply friction
        try {
            double fricMag = Math.min(velocity.magnitude(), FRIC_ACCEL);
            Vector2 fric = velocity.scaleToMagnitude(fricMag);
            velocity = velocity.subtract(fric);
        } catch (ArithmeticException e) {} // Do nothing if velocity is zero

        // Clamp velocity to max speed
        velocity = velocity.clampMagnitude(MAX_SPEED);

        // Face the direction of movement
        if (velocity.magnitude() != 0) {
            facing = Vector2.lerpAngle(facing, velocity.angle(), ROT_ACCEL);
            setWorldRotation(facing);
            setLoopingAnimation(walkingAnimation);
        } else {
            setLoopingAnimation(standingAnimation);
        }

        setWorldPos(getWorldPos().add(new Vector3(velocity.x, 0, velocity.y)));
        Camera.targetPosition(getWorldPos().add(new Vector3(0, 10, 0)));

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
