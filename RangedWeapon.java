import greenfoot.Greenfoot;

/**
 * Weapon that fires projectiles.
 *
 * @author Andrew Wang
 * @author Lucas Fu
 * @version May 2024
 */
public abstract class RangedWeapon extends Weapon {

    @FunctionalInterface
    public static interface ProjectileFactory {
        public Projectile create(Vector3 direction, Vector3 pos, int inaccuracy);
    }

    private ProjectileFactory projFactory;
    private int inaccuracy;
    private int speed;

    public RangedWeapon(Player player, String image, int inaccuracy, int speed, ProjectileFactory projectileFactory) {
        super(player, image);
        projFactory = projectileFactory;
        this.inaccuracy = inaccuracy;
    }

    @Override
    public void update() {
        super.update();
        if(Greenfoot.mouseClicked(null)) {
            attack();
        }
    }

    /**
     * Get the player's mouse direction.
     * 
     * @return the direction of the mouse as a 3d vecotr with no vertical component
     */
    public Vector3 getProjectileDirection() {
        int x = Greenfoot.getMouseInfo().getX()-(int)getPlayer().getWorldX();
        int y = Greenfoot.getMouseInfo().getY()-(int)getPlayer().getWorldY();
        Vector2 direction2 = new Vector2(x, y).normalize();
        Vector3 direction3 = new Vector3(direction2.x, 0, direction2.y);
        return direction3;
    }

    @Override
    public void attack() {
        getProjectileDirection();
        int playerX = (int)getPlayer().getWorldX(); 
        int playerZ = (int)getPlayer().getWorldZ();
        Vector3 spawnPos = new Vector3(playerX, 6, playerZ);
        Vector3 direction = getProjectileDirection().multiply(speed);
        Projectile proj = projFactory.create(direction, spawnPos, inaccuracy);
        getPlayer().getWorld().addSprite(proj, playerX, playerZ);
    }
}
