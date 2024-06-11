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

    @Override
    public void attack() {
        Player player = getPlayer();
        Damage damage = new Damage(player, this, this.damage, player.getWorldPos(), range);

        Vector2 mousePos = MouseManager.getMouseWorldPos();
        Vector2 playerPos = player.getWorldPos().xz;
        double targetAngle = mousePos.subtract(playerPos).angle();
        damage.setAngularRange(targetAngle, sweepAngle);

        try {
            getWorld().getDamages().add(damage);
        }
        catch (NullPointerException e) {} // if the weapon got switched out before doing damage

        swingTimer = new Timer(swingDuration);
    }

    public Timer getSwingTimer(){
        return swingTimer;
    }
}
