import greenfoot.*;

/**
 * A weapon that does something and follows the player rotation.
 *
 * @author Andrew Wang
 * @author Lucas Fu
 * @version May 2024
 */
public abstract class Weapon extends Item {
    private int windupTime;
    private int recastTime;
    private boolean casting;
    private Timer windupTimer;
    private Timer recastTimer;

    /**
     * TODO: add documentation here when weapons are finalized
     * @param player
     * @param image
     * @param windup
     * @param cooldown
     */
    public Weapon(String image, int windup, int cooldown) {
        super(image);
        casting = false;
        windupTime = windup;
        recastTime = cooldown;
        windupTimer = new Timer(0);
        recastTimer = new Timer(0);
    }

    @Override
    public void update() {
        super.update();
        if(isOnGround()) {
            return;
        }

        updateImage();
        MouseInfo mouseInfo = Greenfoot.getMouseInfo();
        if (Greenfoot.mouseClicked(null) && mouseInfo != null
         && mouseInfo.getButton() == 1
         && !casting && recastTimer.ended()) {
            casting = true;
            windupTimer.restart(windupTime);
        }
        if (casting) {
            windup();
        }
        if (casting && windupTimer.ended()) {
            attack();
            casting = false;
            recastTimer.restart(recastTime);
        }
    }

    /**
     * Perform the weapon's attack.
     */
    public abstract void attack();

    /**
     * Perform windup actions. Overriding this is optional.
     */
    public void windup() {};

    /**
     * Get the windup time of the weapon.
     *
     * @return the windup time
     */
    public int getWindup() {
        return windupTime;
    }

    /**
     * Get the progress of the windup as a percentage.
     *
     * @return the windup progress percentage
     */
    public double getWindupProgress() {
        return windupTimer.progress();
    }
}
