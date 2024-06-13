import greenfoot.GreenfootImage;

/**
 * Rick's boombox is a Magic created by the Wand of Rick Astley!
 * <p>
 * This creates a shockwave every 32 frames approximately, just like the beats
 * in Never Gonna Give You Up.
 *
 * @author Lucas Fu
 * @version June 2024
 */
public class RickBoombox extends Magic {
    private long startTime = System.currentTimeMillis();
    private int count;

    public RickBoombox(Vector3 startpos, int inaccuracy) {
        super(startpos.add(new Vector3(0, 5, 0)), inaccuracy, -1, 100000, 0);
        setOriginalImage(new GreenfootImage("megaphone.png"));
    }

    @Override
    public void actionUpdate() {
        if (System.currentTimeMillis() - startTime > 520) {
            startTime = System.currentTimeMillis();
            count++;
            if (count > 16) {
                disappear();
                return;
            }

            Damage dmg = new Damage(getWorld().getPlayer(), this, 10, getWorldPos(), 60);
            dmg.setDamageOwner(true);
            getWorld().getDamages().add(dmg);

            for (Entity e : getWorld().getEntitiesInRange(getWorldPos(), 60)) {
                e.physics.applyForce(e.getWorldPos().subtract(getWorldPos()).normalize().multiply(2));
            }

            for (int theta = 0; theta < 360; theta += 30) {
                for (int i = 0; i < 60; i += 20) {
                    double x = Math.cos(Math.toRadians(theta)) * i;
                    double y = Math.sin(Math.toRadians(theta)) * i;
                    Vector3 particleLocation = getWorldPos().add(Vector3.fromXZ(new Vector2(x, y)));
                    getWorld().addWorldObject(new RickParticle(), particleLocation);
                }
            }
        }
    }
}
