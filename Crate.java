import greenfoot.Greenfoot;
import java.util.Random;

/**
 * ?????
 *
 * @author Andrew Wang
 * @version May 2024
 */
public class Crate extends Feature {
    private Weapon[] contents = new Weapon[] {
        new TestPistol(null),
        new TestSword(null)
    };
    Random rand = new Random();

    /**
     * Create a new Crate with specified id.
     */
    public Crate(FeatureData data) {
        super("crate", data);
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }

    @Override
    public void update() {
        Player player = getWorld().getPlayer();

        // Temporary behavior
        if (new Vector2(player.getWorldX(), player.getWorldZ()).distanceTo(getWorldX(), getWorldZ()) < 15) {
            getWorld().addWorldObject(contents[rand.nextInt(2)], getWorldPos());
            removeFromWorld();
        }
    }
}
