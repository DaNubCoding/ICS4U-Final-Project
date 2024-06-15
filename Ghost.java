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
    private boolean isPhasingIn;
    private boolean isPhasingOut;
    private Timer moveTimer;
    private Timer invisTimer;

    public Ghost() {
        super("ghost_idle");

        setNoticeRange(120);
        setForgetRange(220);
        setHealth(MAX_HP);

        physics.setMaxSpeed(1.2);
        physics.setMaxAccelMag(0.75);
        physics.setAlwaysTurnTowardsMovement(true);

        moveTimer = new Timer(150);
        invisTimer = new Timer(120);
        isPhasingIn = false;
        isPhasingOut = false;
    }

    @Override
    public void idle(Player player) {
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
        isPhasingOut = true;
        setLoopingAnimation(chaseAnimation);
    }

    @Override
    public void forget(Player player) {
        setLoopingAnimation(idleAnimation);
        isPhasingIn = true;
    }

    @Override
    public void engage(Player player) {
        Vector3 playerPos = player.getWorldPos();
        Vector3 enemyPos = getWorldPos();
        double distanceToPlayer = playerPos.subtract(enemyPos).magnitude();

        // move towards the player as long this is not phasing out
        if (!isPhasingOut) physics.moveToNearPlayer(1);

        // if at low transparency, teleport and start phasing in
        if (getTransparency() < 3 && invisTimer.ended()) {
            isPhasingIn = true;
            setWorldPos(player.getWorldPos().addXZ(new Vector2(Math.random() * 300 - 150, Math.random() * 300 - 150)));
        }

        // if close to player, hit them and start phasing out
        if (distanceToPlayer <= 25 && !isPhasingIn && !isPhasingOut) {
            meleePlayer(10, 25);
            isPhasingOut = true;
        }
    }

    public void update() {
        super.update();
        // end of phase-in
        if (getTransparency() < 3) {
            isPhasingOut = false;
            invisTimer.restart();
            hideHealth();
            hideShadow();
        }
        // end of phase-out
        if (getTransparency() > 252) {
            isPhasingIn = false;
            showHealth();
            showShadow();
        }
        // actions to perform while phasing in and out
        if (isPhasingIn) {
            setTransparency(getTransparency() + 3);
        }
        if (isPhasingOut) {
            setTransparency(getTransparency() - 3);
            physics.moveToTarget(getWorldPos().addXZ(new Vector2(getWorldRotation())));
        }
    }

    @Override
    public void damage(Damage damage) {
        if (getTransparency() < 100) return;
        super.damage(damage);
        isPhasingOut = true;
    }

}
