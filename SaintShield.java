import java.util.List;

/**
 * Saint's barrier.
 * <p>
 * Compatibility as the player's shield added by Lucas.
 *
 * @author Stanley
 * @author Lucas Fu
 * @version June 2024
 */
public class SaintShield extends Enemy
{
    private Entity owner;
    private double angle;

    public SaintShield() {
        super("saint_shield");
    }

    public SaintShield(double direction, Entity owner)
    {
        super("saint_shield");
        setHealth(200);
        setNoticeRange(0);
        setForgetRange(0);
        physics.setMaxSpeed(2);
        physics.setMaxAccelMag(1);
        setWorldRotation(direction);
        this.owner = owner;
        this.angle = direction;
        physics.setAlwaysTurnTowardsMovement(false);
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        super.addedToWorld(world);
        getWorld().addCollisionController(new CollisionController(this, 0, 0.8, 0.5));
        if (owner == null) {
            world.removeSprite(this);
        }
    }

    @Override
    public void idle(Player player) {
        setHealth(super.getHealth()-0.05);
        physics.moveToTarget(owner.getWorldPos().xz.add(new Vector2(angle).multiply(20)));

        // player shield custom behaviour
        if (owner instanceof Player) {
            if (getHealth() <= 0) die(null);
            Vector3 pos = getWorldPos();
            List<WorldSprite> inRange = getWorld().getWorldSpritesInRange(pos, 10);
            if (inRange.size() > 0) {
                for (WorldSprite ws : inRange) {
                    if (ws instanceof Projectile) {
                        ((Projectile)ws).disappear();
                        damage(new Damage(this, this, 20, pos, 0));
                    }
                }
            }
        }

    } // Takes damage over time, follows caster around

    @Override
    public void notice(Player player) {}

    @Override
    public void forget(Player player) {}

    @Override
    public void engage(Player player) {}

    @Override
    public void damage(Damage damage) {
        if (damage.getOwner() == owner) return;
        // redirects a portion of all melee damage back
        Entity o = damage.getOwner();
        if (damage.getSource() instanceof MeleeWeapon) {
            // damage owner takes 40% of damage while this only takes 60%
            super.damage(damage.multiply(0.6));
            o.damage(damage.multiply(0.4));
            // special knockback when attacked by statue
            if (!(damage.getSource() instanceof Statue)) {
                Vector3 statuePos = o.getWorldPos();
                Vector3 shieldPos = getWorldPos();
                final Vector3 direction = shieldPos.subtract(statuePos).normalize();
                o.physics.applyForce(direction.multiply(-2));
            }
        } else {
            super.damage(damage);
        }
    }
}
