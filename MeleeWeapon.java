/**
 * Weapon that does point damage within a range.
 *
 * @author Andrew Wang
 * @author Sandra Huang
 * @version May 2024
 */
public abstract class MeleeWeapon extends Weapon {
    private int range;
    private double damage;
    private int sweepAngle;
    private double swingAngle;
    private Timer swingTimer;
    private int swingDuration;
    private Timer unswingTimer;
    private Damage damageObject;

    /**
     * Create a new melee weapon.
     *
     * @param image the image file of this melee weapon
     * @param windup the time spent before attacking
     * @param cooldown the time needed before the next attack
     * @param range the range of this melee weapon
     * @param damage the amount if damage dealt
     * @param sweepAngle the angle of the weapon swing
     * @param swingDuration the time it takes to complete a swing
     */
    public MeleeWeapon(String image, int windup, int cooldown,
                       int range, double damage, int sweepAngle,
                       int swingDuration) {
        super(image, windup, cooldown);
        this.range = range;
        this.damage = damage;
        this.sweepAngle = sweepAngle;
        this.swingDuration = swingDuration;
    }

    @Override
    public void windup() {
        swingAngle = Math.sin(getWindupProgress() * Math.PI / 2) * sweepAngle / 2;
        setWorldRotation(getPlayer().getWorldRotation() + swingAngle);
    }

    @Override
    public void lockToPlayer() {
        super.lockToPlayer();

        if (swingTimer != null) {
            swingAngle = Math.cos(swingTimer.progress() * Math.PI) * sweepAngle / 2;
            if (swingTimer.ended()) {
                swingTimer = null;
                unswingTimer = new Timer(getWindup());
            }
        } else if (unswingTimer != null) {
            swingAngle = -Math.cos(unswingTimer.progress() * Math.PI / 2) * sweepAngle / 2;
            if (unswingTimer.ended()){
                unswingTimer = null;
            }
        }
        setWorldRotation(getPlayer().getWorldRotation() + swingAngle);
    }

    /**
     * Get the angle with which to perform the attack.
     *
     * @return the angle obtained
     */
    public double getTargetAngle() {
        Vector2 mousePos = MouseManager.getMouseWorldPos();
        if (mousePos == null) return -1;
        Vector2 playerPos = getPlayer().getWorldPos().xz;
        double targetAngle = mousePos.subtract(playerPos).angle();
        return targetAngle;
    }

    @Override
    public void attack() {
        Player player = getPlayer();
        Damage damage = new Damage(player, this, this.damage, player.getWorldPos(), range);
        this.damageObject = damage;

        double targetAngle = getTargetAngle();
        if (targetAngle == -1) return;
        damage.setAngularRange(targetAngle, sweepAngle);

        try {
            getWorld().getDamages().add(damage);
        }
        catch (NullPointerException e) {} // if the weapon got switched out before doing damage

        swingTimer = new Timer(swingDuration);
    }

    /**
     * Get the damage object this weapon produces.
     *
     * @return the damage object
     */
    public Damage getDamageObject() {
        return damageObject;
    }

    /**
     * Get the swing timer.
     *
     * @return the swing timer
     */
    public Timer getSwingTimer() {
        return swingTimer;
    }
}
