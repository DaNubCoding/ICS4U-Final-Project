/**
 * The jester, a highly mobile enemy that jumps around the player before swooping
 * in for a big blow.
 * <p>
 * Documentation done by Lucas to his best ability to comprehend this.
 *
 * @author Stanley Wang
 * @version June 2024
 */
public class Jester extends Enemy {
    int MAX_HP = 150;

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
    private static final SoundEffect giggleSound = new SoundEffect("jester_giggle.wav");

    private Timer moveTimer = new Timer(150);
    private Timer attackTimer = new Timer(120);
    private Timer guardTimer = new Timer(0);
    private Timer strafeTimer = new Timer(50);
    private Timer preDashTimer;
    private int particleAngle = 0;
    private double strafeAngle = 0;
    /**
     * The "Attack String" is the amount of short-cooldown attacks left before
     * a longer cooldown.
     * <p>
     * The number of consecutive attacks is equal to the rage level, and the 
     * attackTimer cooldowns are as follows:
     * <ul>
     * <li> 110 - 160 frames when the whole attack string has been performed
     * <li> 40 - 65 frames when there are still attacks left in the string
     */
    private int attackString = 1;
    /**
     * The "actionCount" is the amount of frames the Jester gets to try and 
     * melee the player at range 18. The attack that is trying to be performed 
     * depends on the current "hitBox" value.
     */
    private int actionCount = 0;
    /**
     * The "HitBox" is an attack mechanic:
     * <ul>
     * <li> at level 0: does nothing
     * <li> at level 1: deals 12 damage, setting Hitbox to 0 if it hit
     * <li> at level 2: deals 8 damage, setting Hitbox to 3 if it hit
     * <li> at level 3: spin continuously
     */
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
    }

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

        // The Jester uses a rage mechanic that makes him harder to fight the lower his hp
        int rage = 3-(int)(super.getHealth()/(MAX_HP/3));

        // gets increased max movement speed when at max rage (level 3)
        if (rage == 3) {
            physics.setMaxSpeed(3.2);
        }

        // whenever the jester has actionCounts available to spend (highest priority)
        if (actionCount > 0) {
            // perform hitbox mechanics and reduce actionCount by 1
            if (hitBox == 1) {
                meleePlayer(12, 18);
                if (distance < 18) hitBox = 0;
            }
            if (hitBox == 2) {
                meleePlayer(8, 18);
                if (distance < 18) hitBox = 3;
            }
            if (hitBox > 1) {
                physics.setWorldRotation(getWorldRotation()-20);
            }
            actionCount -= 1;
        } 
        // if jester is guarding, play the guard animation and turn towards the player
        else if (!guardTimer.ended()) {
            setLoopingAnimation(staticAnimations[1]);
            physics.turnTowards(playerPos.xz);
        } 
        // if ready to perform a new attack, consume an attack in the string and perform attack logic
        else if (attackTimer.ended()) {
            attackString -= 1;
            // default attack value is 2 (used when too far)
            int attack = 2;
            hitBox = 0;
            // if the player is close enough, set the attack value to a random value between 1 and 5
            if (distance < 100) {
                attack = (int)(Math.random()*6);
            }
            /**
             * Prepares to dash towards the player when attack value is 1 or 2.
             * This has a 40% chance of happening at close range, 
             * and a 100% chance of happening at long range
             */
            if (attack < 3) {
                physics.turnTowards(playerPos.xz);
                preDashTimer = new Timer(30);
                giggleSound.play();
            } 
            /** 
             * Performs a spin when attack value is 3.
             * This makes the Jester roll towards the general direction of the
             * player and prepare a weak attack lasting 18 frames.
             */
            else if (attack == 3) {
                physics.applyForce(dist.rotateY((Math.random()*40)-20).scaleToMagnitude(4.5));
                playOneTimeAnimation(staticAnimations[3]);
                actionCount = 18;
                hitBox = 2;
            } 
            /**
             * Performs a flip when attack value is 4.
             * This makes the Jester flip towards the general direction of the
             * player and adding 12 frames to the previously prepared attack if 
             * said attack missed.
             */
            else if (attack == 4){
                double a = (Math.random()*50)-25;
                physics.applyForce(dist.rotateY(a).scaleToMagnitude(5));
                playOneTimeAnimation(flippingAnimation);
                actionCount = 12;
                physics.turnTowards(playerPos.xz.rotate(a+90));
            } 
            /**
             * Performs a stab when attack value is 5.
             * This makes the Jester attempt to stab the player for 16 damage
             * before dashing away and adding 12 frames to the previously 
             * prepared attack if said attack missed.
             */
            else {
                meleePlayer(16, 18);
                physics.applyForce(dist.scaleToMagnitude(-4));
                playOneTimeAnimation(staticAnimations[2]);
                physics.turnTowards(playerPos.xz);
                actionCount = 12;
            }

            // perform attackString logic (see attackString documentation)
            if (attackString < 1) {
                attackString = rage;
                attackTimer.restart(110+(int)Math.random() * 50);
            }
            else {
                attackTimer.restart(40+(int)Math.random() * 25);
            }
        // Randomly restarts the guard timer for 45 - 110 frames.
        // This gets more and more likely as the Jester's range meter increases.
        } else if (Math.random() * 500 < rage) {
            guardTimer.restart(45+(int)Math.random() * 65);
        } 
        // if none of the above actions happened, perform movement logic
        else {
            physics.moveToNearPlayer(70);
            // if strafing near the player, accelerate
            if (!strafeTimer.ended() && getWorldPos().distanceTo(playerPos) < 90) {
                physics.accelerate(new Vector2(0.5, 0).rotate(strafeAngle));
            }
            // a low chance to change the strafing angle and restart the strafing
            if (Math.random() < 0.03) {
                strafeAngle = getWorldRotation() + (Math.random() < 0.5 ? 90 : -90);
                strafeTimer.restart(40);
            }
            // perform animations
            if (physics.isMoving())
            {
                setLoopingAnimation(walkingAnimation);
            } else
            {
                setLoopingAnimation(staticAnimations[0]);
            }
        }

        // if the jester has an intention to dash..
        if (preDashTimer != null) {
            /** 
             * If the dash timer has ended, dash toward the player proportional
             * to the distance to the player, preparing a medium-strength attack
             * lasting 15 frames.
             */
            if (preDashTimer.ended()) {
                if (dist.magnitude() < 80) {
                    physics.applyForce(dist.scaleToMagnitude(6));
                } else {
                    physics.applyForce(dist.scaleToMagnitude(9));
                }
                playOneTimeAnimation(staticAnimations[2]);
                actionCount = 15;
                hitBox = 1;
                preDashTimer = null;
            }
            // otherwise, show the attack indication
            else {
                Vector3 offset = new Vector3(10, getHeight(), 0).rotateY(particleAngle);
                Vector3 pos = getWorldPos().add(offset);
                getWorld().addWorldObject(new JesterParticle(), pos);
                particleAngle = (particleAngle + 24) % 360;
                physics.reduceMomentum(0.08);
            }
        }
    }

    /** 
     * Performs "dodges" instead of taking damage when hit with an attack
     * while guard is still on, interrupting any attack this was trying to
     * perform, and canceling any prepared attacks.
     */
    @Override
    public void damage(Damage damage) {
        if (!guardTimer.ended()) {
            hitBox = 0;
            attackTimer.restart(35+(int)Math.random() * 25);
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
