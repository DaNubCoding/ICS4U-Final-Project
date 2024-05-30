import greenfoot.*;

/**
 * The player controlled by the user.
 *
 * @author Martin Baldwin
 * @author Andrew Wang
 * @version May 2024
 */
public class Player extends Sprack {
    private static final double ACCEL = 0.2;
    private static final double FRIC_ACCEL = 0.08;
    private static final double MAX_SPEED = 3.0;

    private static final double ROT_ACCEL = 0.2;
    private static final double ROT_FRIC_ACCEL = 0.1;
    private static final double MAX_ROT_SPEED = 3.0;

    private double speed;
    private double rotSpeed;
    private double cameraTargetRotation;

    public Player() {
        super("car");
    }

    @Override
    public void update() {
        if (Greenfoot.isKeyDown("a")) {
            rotSpeed -= ROT_ACCEL;
        }
        if (Greenfoot.isKeyDown("d")) {
            rotSpeed += ROT_ACCEL;
        }
        // Apply acceleration due to friction
        rotSpeed -= Math.copySign(Math.min(Math.abs(rotSpeed), ROT_FRIC_ACCEL), rotSpeed);
        // Cap rotation speed
        rotSpeed = Math.copySign(Math.min(Math.abs(rotSpeed), MAX_ROT_SPEED), rotSpeed);
        setSpriteRotation(getSpriteRotation() + rotSpeed);

        updateCameraRotation();

        if (Greenfoot.isKeyDown("w")) {
            speed += ACCEL;
        }
        if (Greenfoot.isKeyDown("s")) {
            speed -= ACCEL;
        }
        // Apply acceleration due to friction
        speed -= Math.copySign(Math.min(Math.abs(speed), FRIC_ACCEL), speed);
        // Cap speed
        speed = Math.copySign(Math.min(Math.abs(speed), MAX_SPEED), speed);
        Vector2 forward = new Vector2(getSpriteRotation() - 90);
        setWorldPos(getWorldPos().add(forward.multiply(speed)));
        Camera.targetPosition(getWorldPos());
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
