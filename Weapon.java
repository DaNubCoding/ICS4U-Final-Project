import greenfoot.*;

/**
 * A weapon that does something and follows the player rotation.
 */
public abstract class Weapon extends WorldSprite {
    private Player player;

    public Weapon(Player player, String image) {
        super();
        this.player = player;
        setOriginalImage(new GreenfootImage(image));
    }

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

    public abstract void attack();
}
