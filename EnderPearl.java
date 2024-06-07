import greenfoot.GreenfootImage;

/**
 * A projectile that teleports the owner on hit.
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public class EnderPearl extends Projectile {
    public EnderPearl(Entity owner, Vector3 direction, Vector3 startpos, int inaccuracy) {
        super(owner, direction.setY(8), startpos, inaccuracy, 2000);
        setOriginalImage(new GreenfootImage("test_pistol.png"));
    }

    @Override
    public void movingUpdate() {
        super.movingUpdate();
        setMoveDirection(getMoveDirection().subtract(new Vector3(0, 0.2, 0)));
    }

    @Override
    public boolean hitCondition() {
        boolean entityHit = super.hitCondition();
        return getWorldPos().y <= 0 || entityHit;
    }

    public void hit() {
        Vector3 pos = getWorldPos();
        double y = Math.max(pos.y, 0);
        getOwner().setWorldPos(pos.setY(y));
        if(getOwner() instanceof Player) {
            getWorld().getWorldData().clearSurroundings();
            getWorld().getWorldData().updatePlayerLocation((int)pos.x / 20, (int)pos.z / 20);
            getWorld().getWorldData().generateWorld();
        }
        disappear();
    }
}
