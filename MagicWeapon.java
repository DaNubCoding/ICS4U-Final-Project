/**
 * ?????
 *
 * @author Andrew Wang
 * @version May 2024
 */
public abstract class MagicWeapon extends Weapon {
    public MagicWeapon(Player player, String image) {
        super(player, image);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public abstract void attack();
}
