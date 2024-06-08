/**
 * Weapon that does point damage within a range.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public abstract class MeleeWeapon extends Weapon {
    private int range;
    private double damage;
    private int sweepAngle;
    private Timer swingTimer;
    private double swingAngle;

    /**
     * Create a new melee weapon.
     * TODO: add documentation here when weapons are finalized
     *
     * @param player
     * @param image
     * @param windup
     * @param cooldown
     * @param range
     * @param damage
     * @param sweepAngle
     */
    public MeleeWeapon(Player player, String image, int windup, int cooldown,
                       int range, double damage, int sweepAngle) {
        super(player, image, windup, cooldown);
        this.range = range;
        this.damage = damage;
        this.sweepAngle = sweepAngle;
    }

    @Override
    public void update() {
        super.update();

        if (swingTimer != null) {
            swingAngle = Math.sin(swingTimer.progress() * Math.PI * 2) * sweepAngle / 2;
            if (swingTimer.ended()) {
                swingTimer = null;
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

        swingTimer = new Timer(18);
    }
}
