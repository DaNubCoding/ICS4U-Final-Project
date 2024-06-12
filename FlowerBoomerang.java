/**
 * A boomerang that shoots 5 boomerangs in the shape of a flower.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class FlowerBoomerang extends RangedWeapon {
    private int attacked;
    private Timer multiAttackTimer;
    public FlowerBoomerang() {
        super("flower_boomerang.png", 0, 3, 1, 0, 600, Boomerang::new);
        setCenterOfRotation(new Vector2(9, 9));
        attacked = 10;
        multiAttackTimer = new Timer(0);
    }

    @Override
    public void attack() {
        for (int i = 0; i < 5; i++) {
            getPlayer().physics.setWorldRotation(getPlayer().getWorldRotation() + 72);
            super.attack();
        }
        attacked = 0;
        multiAttackTimer.restart(10);

    }

    @Override
    public void update() {
        super.update();
        if (attacked < 5 && multiAttackTimer.ended() && !isOnGround()) {
            attacked++;
            for (int i = 0; i < 5; i++) {
                getPlayer().physics.setWorldRotation(getPlayer().getWorldRotation() + 72);
                super.attack();
            }
            multiAttackTimer.restart(10);
        }
    }
}
