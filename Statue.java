/**
 * The statue enemy.
 *
 * @author Stanley Wang
 * @version June 2024
 */
public class Statue extends Enemy {
    int MAX_HP = 300;

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

    private Timer attackTimer = new Timer(150);

    public Statue() {
        super("statue_active");
        setNoticeRange(65);
        setForgetRange(400);
        setHealth(MAX_HP);
        physics.setMaxSpeed(0);
        physics.setMaxAccelMag(0);
        physics.setAlwaysTurnTowardsMovement(true);
    }

    @Override
    public void idle(Player player) {
        /*if (moveTimer.ended()) {
        final double distance = Math.random() * 40 + 30;
        final Vector2 offset = new Vector2(Math.random() * 360).multiply(distance);
        physics.moveToTarget(getWorldPos().xz.add(offset));
        moveTimer.restart((int) (Math.random() * 300 + 150));
        }*/
    } // Does nothing

    @Override
    public void notice(Player player) {
        System.out.println("Golem noticed player");
        playOneTimeAnimation(activatingAnimation);
    }

    @Override
    public void forget(Player player) {
        System.out.println("Golem forgot player");
        setLoopingAnimation(dormantAnimation);
    }

    @Override
    public void engage(Player player) {
        Vector3 playerPos = player.getWorldPos();
        Vector3 enemyPos = getWorldPos();

        if (attackTimer.ended()) {
            StatuePearl sPearl = new StatuePearl(
                    this,
                    playerPos.subtract(enemyPos).scaleToMagnitude(3),
                    enemyPos
                );
            getWorld().addSprite(sPearl, 0, 0);
            attackTimer.restart((int) (Math.random() * 65 + super.getHealth()*100/MAX_HP) + 60);
        }
    }

    @Override
    public void damage(Damage damage) {
        // immune to projectile damage
        if (damage.getSource() instanceof Projectile) return;
        super.damage(damage);
        System.out.println("Golem took " + damage.getDamage() + " points of damage");
    }

    public void triggerAttack() {
        Animation attackAnim = attackAnimations[(int) (Math.random() * attackAnimations.length)];
        setLoopingAnimation(attackAnim);
    }
}
