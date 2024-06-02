import greenfoot.GreenfootImage;

/**
 * A projectile shot by the testing pistol, also for testing pyrposes :)
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public class TestProjectile extends Projectile {
    public TestProjectile(Sprack owner, Vector3 direction, Vector3 startpos, int inaccuracy) {
        super(owner, direction, startpos, inaccuracy, 100);
        setOriginalImage(new GreenfootImage("test_pistol.png"));
    }
}
