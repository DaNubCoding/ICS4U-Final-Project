import greenfoot.GreenfootImage;

/**
 * A projectile that travels like a boomerang.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class Boomerang extends Projectile {
    public Boomerang(Entity owner, Vector3 initialVel, Vector3 startPos) {
        super(owner, initialVel, startPos, 200);
        setOriginalImage(new GreenfootImage("statue_projectile.png"));
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
        Damage dmg = new Damage(getOwner(), getOwner(), 5, getWorldPos(), 15);
        getWorld().getDamages().add(dmg);
        for (Entity e : getWorld().getEntitiesInRange(getWorldPos(), 15)) {
            e.physics.applyForce(new Vector2(getWorldRotation()).multiply(2.5));
            e.damage(dmg);
        }
    }
}
