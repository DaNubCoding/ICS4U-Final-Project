import greenfoot.*;

/**
 * A weapon that does something and follows the player rotation.
 *
 * @author Andrew Wang
 * @author Lucas Fu
 * @version May 2024
 */
public abstract class Weapon extends Item {
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
        super(player, image, true);
        casting = false;
        castTime = windup;
        recastTime = cooldown;
        castTimer = new Timer(0);
        recastTimer = new Timer(0);
    }

    @Override
    public void update() {
        super.update();
        if(isOnGround()) {
            return;
        }

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
     * Perform the weapon's attack.
     */
    public abstract void attack();
}
