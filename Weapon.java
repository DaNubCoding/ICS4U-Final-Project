import greenfoot.*;

/**
 * A weapon that does something and follows the player rotation.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public abstract class Weapon extends WorldSprite {
    private Player player;

    public Weapon(Player player, String image) {
        super();
        this.player = player;
        setOriginalImage(new GreenfootImage(image));
    }

    /**
     * Lock the weapon to the player's position and rotation.
     */
    public void lockToPlayer() {
        Vector2 playerPos = player.getWorldPos();
        setWorldPos(new Vector3(playerPos.x, 6, playerPos.y));
        final double rotation = player.getVisualRotation();
        if (rotation > 90 && rotation < 270) {
            setMirrorX(true);
            setScreenRotation(rotation + 180);
        } else {
            setMirrorX(false);
            setScreenRotation(rotation);
        }
    }

    @Override
    public void update() {
        lockToPlayer();
        updateImage();
    }

    /**
     * Perform the weapon's attack.
     */
    public abstract void attack();
}
