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
    }

    @Override
    public void attack() {
        Player player = getPlayer();
        Damage damage = new Damage(player, this, this.damage, player.getWorldPos(), range);

        Vector2 mousePos = MouseManager.getMouseWorldPos();
        Vector2 playerPos = player.getWorldPos().xz;
        double targetAngle = mousePos.subtract(playerPos).angle();
        damage.setAngularRange(targetAngle, sweepAngle);

        damage.setInterval(0);
        getWorld().getDamages().add(damage);
    }
}
