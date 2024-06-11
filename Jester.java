/**
 * The jester enemy.
 *
 * @author Stanley Wang
 * @version June 2024
 */
public class Jester extends Enemy {
    int MAX_HP = 300;

    private static final Animation walkingAnimation = new Animation(12,
            "jester_walk1",
            "jester_idle",
            "jester_walk2",
            "jester_idle"
        );
    private static final Animation flippingAnimation = new Animation(4,
            "jester_flip1",
            "jester_flip2",
            "jester_flip3",
            "jester_flip4"
        );
    private static final Animation[] staticAnimations = new Animation[] {
            new Animation(12, "jester_idle"),
            new Animation(12, "jester_guard"),
            new Animation(12, "jester_stab"),
            new Animation(12, "jester_spin"),
            new Animation(12, "jester_counter")
        };

    private Timer moveTimer = new Timer(150);
    private Timer attackTimer = new Timer(150);
    private Timer guardTimer = new Timer(0);
    private Timer strafeTimer = new Timer(50);
    private double strafeAngle = 0;
    private int attackString = 1;
    private int actionCount = 0;
    private int hitBox = 0;

    public Jester() {
        super("jester_idle");
        setNoticeRange(125);
        setForgetRange(250);
        setHealth(MAX_HP);
        physics.setMaxSpeed(2);
        physics.setMaxAccelMag(0.75);
        physics.setAlwaysTurnTowardsMovement(true);
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        super.addedToWorld(world);
        getWorld().addCollisionController(new CollisionController(this, 8, 0.1, 0.0));
    }

    @Override
    public void idle(Player player) {
        if (moveTimer.ended()) {
            final double distance = Math.random() * 40 + 30;
            final Vector2 offset = new Vector2(Math.random() * 360).multiply(distance);
            physics.moveToTarget(getWorldPos().xz.add(offset));
            moveTimer.restart((int) (Math.random() * 300 + 150));
        }
        if (physics.isMoving()) {
            setLoopingAnimation(walkingAnimation);
        } else {
            setLoopingAnimation(staticAnimations[0]);
        }
    } // Does nothing

    @Override
    public void notice(Player player) {}

    @Override
    public void forget(Player player) {}

    @Override
    public void engage(Player player) {
        Vector3 playerPos = player.getWorldPos();
        Vector3 enemyPos = getWorldPos();
        final double distance = enemyPos.distanceTo(playerPos);
        final Vector3 dist = playerPos.subtract(enemyPos);
        int rage = 3-(int)(super.getHealth()/(MAX_HP/3));

        if (rage == 3) {
            physics.setMaxSpeed(4);
        }

        if (actionCount > 0) {
            if (hitBox == 1) {
                meleePlayer(20, 15);
                if (distance < 15) hitBox = 0;
            }
            if (hitBox == 2) {
                meleePlayer(15, 15);
                if (distance < 15) hitBox = 3;
            }
            if (hitBox > 1) {
                physics.setWorldRotation(getWorldRotation()-20);
            }
            actionCount -= 1;
        } else if (!guardTimer.ended()) {
            setLoopingAnimation(staticAnimations[1]);
            physics.turnTowards(playerPos.xz);
        } else if (attackTimer.ended()) {
            attackString -= 1;
            int attack = 2;
            hitBox = 0;
            if (distance < 100) {
                attack = (int)(Math.random()*4);
            }
            if (attack == 0) {
                physics.applyForce(dist.scaleToMagnitude(6));
                physics.turnTowards(playerPos.xz);
                playOneTimeAnimation(staticAnimations[2]);
                actionCount = 15;
                hitBox = 1;
            } else if (attack == 1) {
                physics.applyForce(dist.rotateY((Math.random()*40)-20).scaleToMagnitude(4.5));
                playOneTimeAnimation(staticAnimations[3]);
                actionCount = 18;
                hitBox = 2;
            } else if (attack == 2){
                double a = (Math.random()*50)-25;
                physics.applyForce(dist.rotateY(a).scaleToMagnitude(5));
                playOneTimeAnimation(flippingAnimation);
                actionCount = 12;
                physics.turnTowards(playerPos.xz.rotate(a+90));
            } else {
                physics.applyForce(dist.scaleToMagnitude(-4));
                playOneTimeAnimation(staticAnimations[2]);
                meleePlayer(25, 15);
                physics.turnTowards(playerPos.xz);
                actionCount = 12;
            }

            if (attackString < 1) {
                attackString = rage;
                attackTimer.restart(130+(int)Math.random() * 50);
            }
            else {
                attackTimer.restart(55+(int)Math.random() * 25);
            }
        } else if (Math.random() * 500 < rage) {
            guardTimer.restart(45+(int)Math.random() * 65);
        } else {
            physics.moveToNearPlayer(70);
            if (!strafeTimer.ended() && getWorldPos().distanceTo(playerPos) < 90) {
                physics.accelerate(new Vector2(0.5, 0).rotate(strafeAngle));
            }
            if (Math.random() < 0.03) {
                strafeAngle = getWorldRotation() + (Math.random() < 0.5 ? 90 : -90);
                strafeTimer.restart(40);
            }
            if (physics.isMoving())
            {
                setLoopingAnimation(walkingAnimation);
            } else
            {
                setLoopingAnimation(staticAnimations[0]);
            }
        }
    }

    @Override
    public void damage(Damage damage) {
        // immune to projectile damage
        if (!guardTimer.ended()) {
            hitBox = 0;
            attackTimer.restart(50+(int)Math.random() * 25);
            playOneTimeAnimation(staticAnimations[4]);
            physics.turnTowards(damage.getCenter().xz);
            physics.applyForce(getWorldPos().subtract(damage.getCenter()).scaleToMagnitude(-3));
            actionCount = 15;
            return;
        }
        super.damage(damage);
    }

    @Override
    public void die() {
        super.die();

        JesterSword item = new JesterSword();
        getWorld().addWorldObject(item, getWorldPos());
        item.physics.applyForce(new Vector3(
            Math.random() * 6 - 3,
            3,
            Math.random() * 6 - 3
        ));
    }
}
