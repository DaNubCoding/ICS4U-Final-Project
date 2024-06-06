/**
 * The golem enemy.
 * <p>
 * It has no behavior at the moment.
 *
 * @author
 * @version June 2024
 */
public class Golem extends Enemy {
    public Golem() {
        super("statue_active");
        setNoticeRange(100);
        setForgetRange(150);
        setHealth(100);
    }

    @Override
    public void idle(Player player) {
        // Do nothing
    }

    @Override
    public void notice(Player player) {
        System.out.println("Golem noticed player");
        // Change animation/texture
    }

    @Override
    public void forget(Player player) {
        System.out.println("Golem forgot player");
        // Change animation/texture
    }

    @Override
    public void engage(Player player) {
        Vector3 playerPos = player.getWorldPos();
        Vector3 enemyPos = getWorldPos();

        if (playerPos.distanceTo(enemyPos) < 20) {
            System.out.println("Golem attacking player");
        }
    }

    @Override
    public void damage(Damage damage) {
        super.damage(damage);
        System.out.println("Golem took " + damage.getDamage() + " points of damage");
    }
}
