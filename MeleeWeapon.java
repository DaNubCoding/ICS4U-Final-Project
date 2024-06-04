/**
 * Weapon that does point damage within a range.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public abstract class MeleeWeapon extends Weapon {
    public MeleeWeapon(Player player, String image, int windup, int cooldown) {
        super(player, image, windup, cooldown);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public abstract void attack();
}
