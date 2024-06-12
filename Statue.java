/**
 * The statue enemy.
 *
 * @author Stanley Wang
 * @version June 2024
 */
public class Statue extends Enemy {
    int MAX_HP = 300;

    private static final Animation dormantAnimation = new Animation(-1, "statue_dormant");
    private static final Animation activeAnimation = new Animation(-1, "statue_active");
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
        super("statue_dormant");
        setNoticeRange(65);
        setForgetRange(400);
        setHealth(MAX_HP);
        physics.setMaxSpeed(0);
        physics.setMaxAccelMag(0);
        physics.setAlwaysTurnTowardsMovement(true);
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        super.addedToWorld(world);
        getWorld().addCollisionController(new CollisionController(this, 8, 1.0, 0.8));
    }

    @Override
    public void idle(Player player) {} // Does nothing

    @Override
    public void notice(Player player) {
        playOneTimeAnimation(activatingAnimation);
        setLoopingAnimation(activeAnimation);
    }

    @Override
    public void forget(Player player) {
        setLoopingAnimation(dormantAnimation);
    }

    @Override
    public void update() {
        super.update();
        physics.reduceMomentum(0.2);
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
            double scalar = 1 - getHealth() / MAX_HP;
            sPearl.physics.applyForce(new Vector2(sPearl.getWorldRotation()).multiply(scalar));
            getWorld().addSprite(sPearl, 0, 0);
            attackTimer.restart((int) (Math.random() * 65 + super.getHealth()*100/MAX_HP) + 60);
        }

        if (Math.random() < 0.1) {
            Vector3 offset = new Vector3(
                Math.random() * 20 - 10,
                getHeight() - Math.random() * 10,
                Math.random() * 20 - 10
            );
            getWorld().addWorldObject(new StatueParticle(), getWorldPos().add(offset));
        }
    }

    @Override
    public void damage(Damage damage) {
        // immune to projectile damage
        if (damage.getSource() instanceof Projectile) {
            if (damage.getSource() instanceof RPGProjectile) {
                super.damage(damage.multiply(0.2));
            }
            return;
        }
        super.damage(damage);
    }

    public void triggerAttack() {
        Animation attackAnim = attackAnimations[(int) (Math.random() * attackAnimations.length)];
        setLoopingAnimation(attackAnim);
        Camera.shake(5, 18);
        for (int i = 0; i < 25; i++) {
            Vector3 offset = new Vector3(
                Math.random() * 20 - 10,
                getHeight() - Math.random() * 10,
                Math.random() * 20 - 10
            );
            getWorld().addWorldObject(new StatueParticle(), getWorldPos().add(offset));
        }
    }

    @Override
    public void die() {
        super.die();

        PearlGun item = new PearlGun();
        getWorld().addWorldObject(item, getWorldPos());
        item.physics.applyForce(new Vector3(
            Math.random() * 6 - 3,
            3,
            Math.random() * 6 - 3
        ));
    }
}
