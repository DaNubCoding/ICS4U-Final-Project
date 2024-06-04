import greenfoot.GreenfootImage;

/**
 * Test class for magics
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public class CrateBomb extends Magic {
    public CrateBomb(Vector3 startpos, int inaccuracy) {
        super(startpos.add(new Vector3(0, 300, 0)), inaccuracy, 0, 10000, 10, 100);
        setOriginalImage(new GreenfootImage("test_pistol.png"));
    }

    @Override
    public void actionUpdate() {
        setWorldPos(getWorldPos().subtract(new Vector3(0, 5, 0)));
    }

    @Override
    public void disappear() {
        TreeCanopy test = new TreeCanopy("tree_canopy");
        getWorld().addSprite(test, 0, 0);
        test.setWorldPos(getWorldPos());
        super.disappear();
    }
}
