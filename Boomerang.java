import greenfoot.GreenfootImage;

/**
 * A projectile that travels like a boomerang.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class Boomerang extends Projectile {
    private Timer hitCooldown = new Timer(0);

    public Boomerang(Entity owner, Vector3 initialVel, Vector3 startPos) {
        super(owner, initialVel, startPos, 300);
        setOriginalImage(new GreenfootImage("flower_projectile.png"));
        physics.setAffectedByFrictionalForces(true);
        physics.setWorldRotation(owner.getWorldRotation());
    }

    @Override
    public void movingUpdate() {
        physics.setWorldRotation(getWorldRotation() + 5);
        physics.reduceMomentum(0.2);
        physics.applyForce(new Vector2(getWorldRotation()));
    }

    @Override
    public void hit() {
        if (!hitCooldown.ended()) return;

        Damage dmg = new Damage(getOwner(), this, 5, getWorldPos(), 20);
        getWorld().getDamages().add(dmg);
        for (Entity e : getWorld().getEntitiesInRange(getWorldPos(), 20)) {
            e.physics.applyForce(new Vector2(getWorldRotation()).multiply(6.0));
        }
        hitCooldown.restart(10);
    }
}
