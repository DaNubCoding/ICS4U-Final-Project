import greenfoot.GreenfootImage;

public class SaintShieldMaker extends Magic {
    public SaintShieldMaker(Vector3 startpos, double inaccuracy) {
        super(startpos, inaccuracy, -1, 3600, 0);
        setOriginalImage(new GreenfootImage("manual.png"));
    }

    @Override
    public void actionUpdate() {
        getWorld().addWorldObject(new SaintShield(Math.random() * 360, getWorld().getPlayer()), getWorldPos());
        disappear();
    }
}
