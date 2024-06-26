/**
 * A type of weapon that creates world-anchored attacks by summoning {@link Magic}s.
 *
 * @author Andrew Wang
 * @author Lucas Fu
 * @version May 2024
 */
public abstract class MagicWeapon extends Weapon {

    @FunctionalInterface
    public static interface MagicFactory {
        public Magic create(Vector3 pos, int inaccuracy);
    }

    private MagicFactory magicFactory;
    private int inaccuracy;
    private int castCount;

    /**
     * Create a new magic weapon with all the specifications.
     * 
     * @param image the image file name that this weapon will use
     * @param inaccuracy the maximum radius
     * @param castCount the number of magics created per cast
     * @param windup the time it takes to cast after clicking
     * @param cooldown the time needed to wait before clicking again
     * @param magicFactory the magic to be created (use ::new)
     */
    public MagicWeapon(String image, int inaccuracy, 
                       int castCount, int windup, int cooldown, 
                       MagicFactory magicFactory) {
        super(image, windup, cooldown);
        this.inaccuracy = inaccuracy;
        this.castCount = castCount;
        this.magicFactory = magicFactory;
    }

    @Override
    public void update() {
        super.update();
    }

    /**
     * Find the target location the player is aiming at.
     * 
     * @return the location that is targeted
     */
    public Vector3 findTargetLocation() {
        Vector2 mousePos = MouseManager.getMouseWorldPos();
        Vector3 targetLocation = new Vector3(mousePos.x, 0, mousePos.y);
        return targetLocation;
    }

    @Override
    public void attack() {
        Vector3 targetLocation = findTargetLocation();
        for(int i = 0; i < castCount; i++) {
            Magic magic = magicFactory.create(targetLocation, inaccuracy);
            getPlayer().getWorld().addSprite(magic, 0, 0);
        }
    }
}
