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

        if (swingTimer != null) {
            swingAngle = -1 * Math.sin(swingTimer.progress() * Math.PI / 2) * sweepAngle;
            if (swingTimer.ended()) {
                swingTimer = null;
                unswingTimer = new Timer(unswingDuration);
            }
        }else if(unswingTimer != null){
            swingAngle = -1 * Math.sin(unswingTimer.progress() * Math.PI / 2 + Math.PI / 2) * sweepAngle;
            if (unswingTimer.ended()) {
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

        getWorld().getDamages().add(damage);

        swingTimer = new Timer(swingDuration);
    }

    public Timer getSwingTimer(){
        return swingTimer;
    }
}
