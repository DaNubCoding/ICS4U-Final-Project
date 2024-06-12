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
    private Random rand = new Random();
    private String treeType;

    public CanopyBomb(Vector3 startpos, int inaccuracy) {
        super(startpos.add(new Vector3(0, 300, 0)), inaccuracy, 1, 10000, 10, 100);
        treeType = treeList[rand.nextInt(treeList.length)];
        setOriginalImage(SprackView.getView("tree_" + treeType + "_canopy")
            .getTransformedImage(Camera.getRotation(), Camera.getZoom()));
        physics.setAffectedByGravity(true);
        rand = new Random();
    }

    @Override
    public void actionUpdate() {
        physics.update();
    }

    @Override
    public void disappear() {
        PerishableCanopy canopy = new PerishableCanopy("tree_" + treeType + "_canopy");
        getWorld().addWorldObject(canopy, getWorldPos());
        Damage dmg = new Damage(getWorld().getPlayer(), this, 5, getWorldPos(), 30);
        dmg.setInterval(50);
        dmg.setLifetime(600);
        dmg.setDamageOwner(true);
        getWorld().getDamages().add(dmg);
        super.disappear();
    }
}
