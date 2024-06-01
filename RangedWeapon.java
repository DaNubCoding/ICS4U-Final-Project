/**
 * Weapon that fires projectiles.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public abstract class RangedWeapon extends Weapon {
    public RangedWeapon(Player player, String image) {
        super(player, image);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public abstract void attack();
}
