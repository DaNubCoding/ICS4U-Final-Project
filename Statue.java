/**
 * The statue enemy.
 *
 * @author
 * @version June 2024
 */
public class Statue extends Enemy {
    private static final Animation activeAnimation = new Animation(-1, "statue_active");
    private static final Animation dormantAnimation = new Animation(-1, "statue_dormant");
    private static final Animation activatingAnimation = new Animation(12,
        "statue_activating1",
        "statue_activating2"
    );
    private static final Animation[] attackAnimations = new Animation[] {
        new Animation(12, "statue_attack1"),
        new Animation(12, "statue_attack2"),
        new Animation(12, "statue_attack3"),
        new Animation(12, "statue_attack4"),
        new Animation(12, "statue_attack5"),
    };

    private Timer moveTimer = new Timer(300);
    private Timer attackTimer = new Timer(60);

    public Statue() {
        super("statue_active");
        setNoticeRange(100);
        setForgetRange(150);
        setHealth(100);
        setMaxSpeed(1.0);
        setMaxAccel(0.25);
        setAlwaysTurnTowardsMovement();
    }

    @Override
    public void idle(Player player) {
        if (moveTimer.ended()) {
            final double distance = Math.random() * 40 + 30;
            final Vector2 offset = new Vector2(Math.random() * 360).multiply(distance);
            moveToTarget(getWorldPos().xz.add(offset));
            moveTimer.restart((int) (Math.random() * 300 + 150));
        }
    }

    @Override
    public void notice(Player player) {
        System.out.println("Golem noticed player");
        playOneTimeAnimation(activatingAnimation);
        setLoopingAnimation(activeAnimation);
    }

    @Override
    public void forget(Player player) {
        System.out.println("Golem forgot player");
        forgetTarget();
        setLoopingAnimation(dormantAnimation);
    }

    @Override
    public void engage(Player player) {
        moveToNearPlayer(15);

        Vector3 playerPos = player.getWorldPos();
        Vector3 enemyPos = getWorldPos();

        if (playerPos.distanceTo(enemyPos) < 20 && attackTimer.ended()) {
            System.out.println("Golem attacking player");
            meleePlayer(10, 20);
            Animation attackAnim = attackAnimations[(int) (Math.random() * attackAnimations.length)];
            playOneTimeAnimation(attackAnim);
            attackTimer.restart();
        }
    }

    @Override
    public void damage(Damage damage) {
        // immune to projectile damage
        if (damage.getSource() instanceof Projectile) return;
        super.damage(damage);
        System.out.println("Golem took " + damage.getDamage() + " points of damage");
    }
}
