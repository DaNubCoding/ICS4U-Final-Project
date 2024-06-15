/**
 * The Ghost is slightly stronger than the basic skeleton, able to phase in and
 * out of existence!
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class Ghost extends Enemy {
    private static final Animation idleAnimation = new Animation(-1, "ghost_idle");
    private static final Animation chaseAnimation = new Animation(-1, "ghost_chase");

    private final int MAX_HP = 75;
    private Timer moveTimer;
    private Timer invisTimer;
    private boolean invisible;
    private Timer attackTimer;

    public Ghost() {
        super("ghost_idle");

        setNoticeRange(120);
        setForgetRange(220);
        setHealth(MAX_HP);

        physics.setMaxSpeed(1.2);
        physics.setMaxAccelMag(0.75);
        physics.setAlwaysTurnTowardsMovement(true);

        moveTimer = new Timer(150);
        invisTimer = new Timer(240);
        attackTimer = new Timer(25);
    }

    @Override
    public void idle(Player player) {
        setTransparency(255);
        // random movements
        if (moveTimer.ended()) {
            final double distance = Math.random() * 40 + 30;
            final Vector2 offset = new Vector2(Math.random() * 360).multiply(distance);
            physics.moveToTarget(getWorldPos().xz.add(offset));
            moveTimer.restart((int) (Math.random() * 300 + 150));
        }
    }

    @Override
    public void notice(Player player) {
        setLoopingAnimation(chaseAnimation);
    }

    @Override
    public void forget(Player player) {
        setLoopingAnimation(idleAnimation);
    }

    @Override
    public void engage(Player player) {
        Vector3 playerPos = player.getWorldPos();
        Vector3 enemyPos = getWorldPos();
        double distanceToPlayer = playerPos.subtract(enemyPos).magnitude();

        if (invisTimer.ended()) {
            if (invisible) {
                setWorldPos(player.getWorldPos().addXZ(new Vector2(Math.random() * 150 - 75, Math.random() * 150 - 75)));
                showHealth();
                showShadow();
            } else {
                hideHealth();
                hideShadow();
            }
            invisible = !invisible;
            invisTimer.restart((int) (Math.random() * 300 + 150));
        }

        if (invisible) {
            setTransparency(Math.max(0, getTransparency() - 3));
        } else {
            setTransparency(Math.min(255, getTransparency() + 6));
            physics.moveToNearPlayer(10);
            if (distanceToPlayer < 50 && attackTimer.ended()) {
                meleePlayer(10, 25);
                attackTimer.restart();
            }
        }
    }

    @Override
    public void damage(Damage damage) {
        if (getTransparency() < 100) return;
        super.damage(damage);
        if (getHealth() < MAX_HP / 2) {
            invisTimer.restart(20);
        }
    }

}
