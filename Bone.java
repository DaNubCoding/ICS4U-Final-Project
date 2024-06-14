import greenfoot.GreenfootImage;

/**
 * A standard projectile with a constantly increasing velocity and pierces thrice.
 * <p>
 * <i> How do skeletons even toss an accelerating bone? </i>
 * 
 * @author Lucas Fu
 * 
 */
public class Bone extends Projectile {
    private int pierces;

    public Bone(Entity owner, Vector3 direction, Vector3 startpos) {
        super(owner, direction, startpos, 200);
        setOriginalImage(new GreenfootImage("repeater_projectile.png"));
        physics.setWorldRotation(direction.xz.angle());
        //TODO: bone.png
        pierces = 3;
    }

    @Override
    public void movingUpdate() {
        // constantly accelerate
        physics.applyForce(new Vector2(getWorldRotation()).multiply(0.05));
    }

    @Override
    public void hit() {
        // only hits the player, but will consume a pierce charge upon hitting anything
        Damage dmg = new Damage(getOwner(), this, 1, getWorldPos(), 10);
        dmg.setTarget(getWorld().getPlayer());
        getWorld().getDamages().add(dmg);
        if (pierces-- <= 0) disappear();
    }
}
