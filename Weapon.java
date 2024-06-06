import greenfoot.*;

/**
 * A weapon that does something and follows the player rotation.
 *
 * @author Andrew Wang
 * @author Lucas Fu
 * @version May 2024
 */
public abstract class Weapon extends WorldSprite {
    private Player player;
    private int castTime;
    private int recastTime;
    private boolean casting;
    private Timer castTimer;
    private Timer recastTimer;

    /**
     * TODO: add documentation here when weapons are finalized
     * @param player
     * @param image
     * @param windup
     * @param cooldown
     */
    public Weapon(Player player, String image, int windup, int cooldown) {
        super();
        this.player = player;
        casting = false;
        castTime = windup;
        recastTime = cooldown;
        castTimer = new Timer(0);
        recastTimer = new Timer(0);
        setOriginalImage(new GreenfootImage(image));
    }

    /**
     * Lock the weapon to the player's hand position and rotation.
     */
    public void lockToPlayer() {
        Vector3 playerPos = player.getWorldPos();
        // transform player's bottom-center location to hand location
        Vector3 handOffset = new Vector3(5, 8, -5).rotateY(player.getWorldRotation());
        setWorldPos(playerPos.add(handOffset));

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
        if (Greenfoot.mouseClicked(null)
         && Greenfoot.getMouseInfo().getButton() == 3
         && !casting && recastTimer.ended()) {
            casting = true;
            castTimer.restart(castTime);
        }
        if (casting && castTimer.ended()) {
            attack();
            casting = false;
            recastTimer.restart(recastTime);
        }
    }

    /**
     * Get the player to which this weapon is attached to.
     * @return
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the SpriteStackingWorld the weapon is in.
     *
     * @return the SpriteStackingWorld the weapon is in
     */
    public SpriteStackingWorld getWorld() {
        return player.getWorld();
    }

    /**
     * Perform the weapon's attack.
     */
    public abstract void attack();
}
