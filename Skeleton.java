/**
 * The Skeleton is your average enemy, fully kitted out for you to test your 
 * weapons!
 * <p>
 * Despite it being a damage sponge, if you let them alone for too long, you'll
 * regret it!
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public class Skeleton extends Enemy {
    private static final Animation walkingAnimation = new Animation(60, 
            "skeleton_step1",
            "skeleton_step2"
        );
    private static final Animation idleAnimation = new Animation(-1, "skeleton_idle");
    private static final Animation attackAnimation = new Animation(4, "skeleton_attack");

    private final int MAX_HP = 50;
    private Timer moveTimer;
    private Timer punchTimer;
    private Timer shootTimer;

    public Skeleton() {
        super("skeleton_idle");

        setNoticeRange(120);
        setForgetRange(250);
        setHealth(MAX_HP);

        physics.setMaxSpeed(0.8);
        physics.setMaxAccelMag(0.75);
        physics.setAlwaysTurnTowardsMovement(true);

        moveTimer = new Timer(150);
        punchTimer = new Timer(60);
        shootTimer = new Timer(180);
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        super.addedToWorld(world);
        getWorld().addCollisionController(new CollisionController(this, 8, 0.1, 0.0));
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
        if (physics.isMoving()) {
            setLoopingAnimation(walkingAnimation);
        } else {
            setLoopingAnimation(idleAnimation);
        }
    } 

    @Override
    public void notice(Player player) {
        playOneTimeAnimation(attackAnimation);
        Vector3 playerPos = player.getWorldPos();
        Vector3 enemyPos = getWorldPos();
        Bone bone = new Bone(this, playerPos.subtract(enemyPos).normalize().multiply(5), enemyPos);
        getWorld().addWorldObject(bone, enemyPos);
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

        physics.moveToNearPlayer(15);

        if (distanceToPlayer <= 25 && punchTimer.ended()) {
            meleePlayer(2, 25);
            punchTimer.restart();
        }

        if (distanceToPlayer > 25 && shootTimer.ended()) {
            Bone bone = new Bone(this, playerPos.subtract(enemyPos).normalize(), enemyPos);
            getWorld().addWorldObject(bone, enemyPos);
            shootTimer.restart();
        }
    }
}
