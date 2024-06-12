/**
 * A boomerang that shoots 5 boomerangs in the shape of a flower.
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public class FlowerBoomerang extends RangedWeapon {
    public FlowerBoomerang() {
        super("repeater.png", 0, 3, 1, 0, 200, Boomerang::new);
    }

    @Override
    public void attack() {
        for (int i = 0; i < 5; i++) {
            getPlayer().physics.setWorldRotation(getPlayer().getWorldRotation() + 72);
            super.attack();
        } 
    }
}
