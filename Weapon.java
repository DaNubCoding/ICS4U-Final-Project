import greenfoot.*;

/**
 * A weapon that does attacks and follows the player rotation.
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
     * Create a new weapon with windup and cooldown
     * @param image the image this should use
     * @param windup the amount of delay after a click before an attack
     * @param cooldown the amount of delay before the next click gets registered
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
        // if left click and not attacking and not on cooldown, attack.
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
