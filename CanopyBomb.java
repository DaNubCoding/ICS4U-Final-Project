import greenfoot.GreenfootImage;
import java.util.Random;
/**
 * A canopy bomb that falls from the sky, dealing damage and creating a canopy
 * on impact.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class CanopyBomb extends Magic {
    private PhysicsController physics = new PhysicsController(this);
    private static final String[] treeList = {"oak", "willow"};
    private Random rand;

    public CanopyBomb(Vector3 startpos, int inaccuracy) {
        super(startpos.add(new Vector3(0, 300, 0)), inaccuracy, 1, 10000, 10, 100);
        setOriginalImage(new GreenfootImage("pistol.png"));
        physics.setAffectedByGravity(true);
        rand = new Random();
    }

    @Override
    public void actionUpdate() {
        physics.update();
    }

    @Override
    public void disappear() {
        PerishableCanopy canopy = new PerishableCanopy("tree_" + treeList[rand.nextInt(treeList.length)] + "_canopy");
        getWorld().addWorldObject(canopy, getWorldPos());
        Damage dmg = new Damage(null, this, 5, getWorldPos(), 30);
        dmg.setInterval(50);
        dmg.setLifetime(600);
        getWorld().getDamages().add(dmg);
        super.disappear();
    }
}
