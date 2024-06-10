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
    private Timer unswingTimer1;
    private Timer unswingTimer2;
    private int unswingDuration;

    /**
     * Create a new melee weapon.
     * TODO: add documentation here when weapons are finalized
     *
     * @param image
     * @param windup
     * @param cooldown
     * @param range
     * @param damage
     * @param sweepAngle
     * @param swingDuration
     * @param unswingDuration
     */
    public MeleeWeapon(String image, int windup, int cooldown,
                       int range, double damage, int sweepAngle,
                       int swingDuration, int unswingDuration) {
        super(image, windup, cooldown);
        this.range = range;
        this.damage = damage;
        this.sweepAngle = sweepAngle;
        this.swingDuration = swingDuration;
        this.unswingDuration = unswingDuration;
    }

    @Override
    public void lockToPlayer() {
        super.lockToPlayer();

        if (unswingTimer1 != null) {
            swingAngle = Math.sin(unswingTimer1.progress() * Math.PI / 2) * sweepAngle / 2;
            if (unswingTimer1.ended()) {
                unswingTimer1 = null;
                swingTimer = new Timer(swingDuration);
            }
        } else if (swingTimer != null) {
            swingAngle = Math.cos(swingTimer.progress() * Math.PI) * sweepAngle / 2;
            if (swingTimer.ended()) {
                swingTimer = null;
                unswingTimer2 = new Timer(unswingDuration / 2);
            }
        } else if (unswingTimer2 != null) {
            swingAngle = -Math.cos(unswingTimer2.progress() * Math.PI / 2) * sweepAngle / 2;
            if (unswingTimer2.ended()){
                unswingTimer2 = null;
            }
        }
        setWorldRotation(getPlayer().getWorldRotation() + swingAngle);
    }

    @Override
    public void attack() {
        Player player = getPlayer();
        Damage damage = new Damage(player, this, this.damage, player.getWorldPos(), range);

        Vector2 mousePos = MouseManager.getMouseWorldPos();
        Vector2 playerPos = player.getWorldPos().xz;
        double targetAngle = mousePos.subtract(playerPos).angle();
        damage.setAngularRange(targetAngle, sweepAngle);

        getWorld().getDamages().add(damage);

        if (unswingDuration != 0) {
            unswingTimer1 = new Timer(unswingDuration / 2);
        } else {
            swingTimer = new Timer(swingDuration);
        }
    }

    public Timer getSwingTimer(){
        return swingTimer;
    }
}
