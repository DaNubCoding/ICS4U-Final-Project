import java.util.List;
import java.util.Random;
import greenfoot.GreenfootImage;
import java.util.ArrayList;

/**
 * A projectile shot by the testing pistol, also for testing pyrposes :)
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class SaintFire extends Projectile {
    private Entity owner;
    private List<Sprack> hit;

    public SaintFire(Entity owner, Vector3 initialVel, Vector3 startpos) {
        super(owner, initialVel.rotateY(Math.random()*30-15), startpos, (int)(Math.random()*50)+125);
        setOriginalImage(new GreenfootImage("holy_fire.png"));
        physics.setAffectedByGravity(false);
        setWorldRotation(initialVel.xz.angle());
        this.owner = owner;
        this.hit = new ArrayList<Sprack>();
    }

    @Override
    public boolean hitCondition() {
        List<Sprack> l = getWorld().getSpracksInRange(getWorldPos(), 5);
        if(l.size() > 0 && l.contains(getOwner())) return false;
        return l.size() > 0;
    }

    @Override
    public void hit() {
        Damage dmg = new Damage(owner, owner, 0.2, getWorldPos(), 5);
        getWorld().getDamages().add(dmg);
        List<Sprack> l = getWorld().getSpracksInRange(getWorldPos(), 5);
        for (Sprack s:l) {
            if(hit.contains(s) || s instanceof Saint || s instanceof SaintShield || s instanceof Feature) continue;
            else {
                ((Entity)s).damage(dmg);
                hit.add(s);
            }
        }
    }
}
