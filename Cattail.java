import greenfoot.GreenfootImage;

/**
 * A cattail is a background decoration that exists near water.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class Cattail extends WorldSprite {
    private static final GreenfootImage[] CATTAIL_VARIATIONS = {new GreenfootImage("cattail1.png"), new GreenfootImage("cattail2.png"), new GreenfootImage("cattail3.png"),
        new GreenfootImage("cattail4.png")};

    public Cattail(int cattailType) {
        setOriginalImage(CATTAIL_VARIATIONS[cattailType]);
    }

    @Override
    public void update(){
        setWorldRotation(Camera.getRotation());
    }
}
