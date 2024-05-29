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
    private static final double MAX_ROT_SPEED = 1.5;

    private double speed;
    private double rotSpeed;

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
        final double rotation = getSpriteRotation() + rotSpeed;
        setSpriteRotation(rotation);
        Camera.targetRotation(rotation);

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
        Vector2 forward = new Vector2(rotation - 90);
        setWorldPos(getWorldPos().add(forward.multiply(speed)));
        Camera.targetPosition(getWorldPos());
    }
}
