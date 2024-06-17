/**
 * The Saint, a defensive ranged enemy.
 * <p>
 * Documentation is done by Lucas to the best of his ability.
 *
 * @author Stanley
 * @version June 2024
 */
public class Saint extends Enemy
{
    int MAX_HP = 200;

    private static final Animation[] saintAnimations = new Animation[] {
            new Animation(12, "saint_idle"),
            new Animation(12, "saint_normal_charge"),
            new Animation(12, "saint_normal_spell"),
            new Animation(12, "saint_front_charge"),
            new Animation(12, "saint_front_spell"),
            new Animation(12, "saint_side_charge"),
            new Animation(12, "saint_side_spell"),
        };
    private static final SoundEffect summonSound = new SoundEffect("saint_summon.wav");
    private static final SoundEffect attackSound = new SoundEffect("saint_attack.wav");

    private Timer moveTimer = new Timer(185);
    private Timer attackTimer = new Timer(250);

    private double charge = 8;
    /**
     * Attack Types are as follows:
     * <ul>
     * <li> attackType 1: Saint Blasts (homing projectiles)
     * <li> attackType 2: Saint Shield (summon a {@link SaintShield})
     * <li> attackType 3: Saint Fire   (cover a wide arc with yellow circles)
     */
    private int attackType = 0;
    // the number of SaintBlasts left to shoot
    private int holyBlasts = 0;
    /**
     * Attack Phases are as follows:
     * <ul>
     * <li> phase 0: choosing an attack based on charge
     * <li> phase 1: setting the appropriate animation
     * <li> phase 2: perform simple attacks (SaintBlast and SaintShield)
     * <li> phase 3: perform optional complex attack (SaintFire)
     * <li> phase 4: cooldown phase
     */
    private int attackPhase = 0;

    public Saint()
    {
        super("saint_idle");
        setNoticeRange(300);
        setForgetRange(375);
        setHealth(MAX_HP);
        physics.setMaxSpeed(1);
        physics.setMaxAccelMag(0.3);
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        super.addedToWorld(world);
        getWorld().addCollisionController(new CollisionController(this, 8, 0.1, 0.0));
    }

    @Override
    public void idle(Player player) {
        physics.setAlwaysTurnTowardsMovement(true);
        setLoopingAnimation(saintAnimations[0]);
        if (moveTimer.ended()) {
            final double distance = Math.random() * 30 + 20;
            final Vector2 offset = new Vector2(Math.random() * 360).multiply(distance);
            physics.moveToTarget(getWorldPos().xz.add(offset));
            moveTimer.restart((int) (Math.random() * 250 + 125));
        }
    }

    @Override
    public void notice(Player player) {
        physics.setAlwaysTurnTowardsMovement(false);
        final Vector3 dist = player.getWorldPos().subtract(getWorldPos());
        physics.applyForce(dist.scaleToMagnitude(-3));
    }

    @Override
    public void forget(Player player) {}

    @Override
    public void engage(Player player) {
        Vector3 playerPos = player.getWorldPos();
        Vector3 enemyPos = getWorldPos();
        final double distance = enemyPos.distanceTo(playerPos);
        final Vector3 dist = playerPos.subtract(enemyPos);

        // when reaching phase 4 and ready to attack, reset phases and pause attacks for 200 frames
        if (attackPhase == 4 && attackTimer.ended()) {
            attackPhase = 0;
            rageTime(200);
            setLoopingAnimation(saintAnimations[0]);
        }

        // when reaching phase 3...
        if (attackPhase == 3) {
            // if current selecting SaintFire, spew a bunch of SaintFire
            if (attackType == 3) {
                if (Math.random() < 0.2) {
                    setLoopingAnimation(saintAnimations[4]);
                    SaintFire sFire = new SaintFire(
                            this,
                            playerPos.subtract(enemyPos).scaleToMagnitude(Math.random()*2+1.5),
                            enemyPos.add(dist.normalize().multiply(4))
                        );
                    getWorld().addSprite(sFire, 0, 0);
                }
            }
            // if ready to attack...
            if (attackTimer.ended()) {
                // if SaintFire was just used, switch to phase 4 and pause attacks for 30 frames
                if (attackType == 3) {
                    attackPhase = 4;
                    rageTime(30);
                    setLoopingAnimation(saintAnimations[3]);
                }
                // if SaintBlast was previously used and there are still more left,
                // pause attacks for 30 frames and continue using SaintBlasts
                else if (attackType == 1 && holyBlasts > 0) {
                    attackPhase = 1;
                    rageTime(30);
                }
                // otherwise, pause attacks for 200 frames
                else {
                    attackPhase = 0;
                    rageTime(200);
                    setLoopingAnimation(saintAnimations[0]);
                }
            }
        }

        // when reaching phase 2...
        if (attackPhase == 2) {
            // if currently selecting SaintBlast, gain charge proportional to lost hp,
            // and fire some SaintBlasts while raging for 30 frames
            if (attackType == 1) {
                setLoopingAnimation(saintAnimations[2]);
                charge += 2-super.getHealth()/MAX_HP;
                holyBlasts -= 1;
                SaintBlast sBlast = new SaintBlast(
                        this,
                        playerPos.subtract(enemyPos).scaleToMagnitude(3.5),
                        enemyPos,
                        (Entity)player
                    );
                getWorld().addSprite(sBlast, 0, 0);
                rageTime(30);
                // if there are no more holy blasts to use, pause attacks for 45 frames
                // before switching to phase 3
                if (holyBlasts == 0) {
                    rageTime(45);
                    attackPhase = 3;
                }
            }
            // if currently selecting ShieldSummon, create a SaintShield and pause attacks for 40 frames
            else if (attackType == 2) {
                setLoopingAnimation(saintAnimations[6]);
                getWorld().addWorldObject(new SaintShield(getWorldRotation(), this), enemyPos.add(dist.normalize().multiply(15)));
                summonSound.play();
                rageTime(40);
            }
            // if currently selecting SaintFire, restart attack timer for 250 frames
            else if (attackType == 3) {
                attackTimer.restart(250);
            }
            // always switch to phase 3 after phase 2
            attackPhase = 3;
        }

        // when reaching phase 1...
        if (attackPhase == 1) {
            // switch animations based on currently selected attack, and move to phase 2
            if (attackType == 1) {
                setLoopingAnimation(saintAnimations[1]);
            } else if (attackType == 2) {
                switchAnimation(3,4,75);
            } else if (attackType == 3) {
                switchAnimation(5,6,85);
            }
            if (attackTimer.ended()) {
                attackPhase = 2;
            }
        }

        // when reaching phase 0, determine the next attack to use based on charge meter
        if (attackPhase == 0 && attackTimer.ended()) {
            // consume 20 charges to schedule a saintFire attack in 150 frames
            if (charge > 20) {
                attackType = 3;
                charge -= 20;
                attackSound.play();
                rageTime(150);
            }
            // 50% chance to consume 8 charges to schedule a shield summon in 65 frames
            else if (charge >= 8 && Math.random() < 0.5) {
                attackType = 2;
                charge -= 8;
                rageTime(65);
            }
            // schedule between 3 to 6 holy blasts in 35 seconds
            else {
                holyBlasts = (int)(Math.random()*4+3);
                attackType = 1;
                rageTime(35);
            }
            attackPhase = 1;
        }

        // attempt to stay between 90 and 150 distance from the player
        physics.turnTowards(playerPos.xz);
        if (distance < 90) {
            physics.moveToTarget(getWorldPos().xz.add(dist.xz.multiply(-0.5)));
            if (distance < 45 && Math.random() < 0.05 && super.getHealth() < MAX_HP/2) {
                physics.applyForce(dist.scaleToMagnitude(-3));
            }
        } else if (distance > 150) {
            physics.moveToTarget(getWorldPos().xz.add(dist.xz.multiply(1)));
        }
    }

    private void switchAnimation(int frame1, int frame2, int progress) {
        if (attackTimer.progress() > (double)(progress/100)) {
            setLoopingAnimation(saintAnimations[frame2]);
        } else {
            setLoopingAnimation(saintAnimations[frame1]);
        }
    }

    private void rageTime(int time) {
        boolean rage = (super.getHealth()/MAX_HP < 0.6);
        if (rage) {
            attackTimer.restart((int)(time/3));
        } else {
            attackTimer.restart(time);
        }
    }

    @Override
    public void die(Entity killer) {
        super.die(killer);

        TomeOfProtectionV item = new TomeOfProtectionV();
        getWorld().addWorldObject(item, getWorldPos());
        item.physics.applyForce(new Vector3(
            Math.random() * 6 - 3,
            3,
            Math.random() * 6 - 3
        ));
    }
}
