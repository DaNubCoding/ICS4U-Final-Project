import greenfoot.GreenfootImage;

/**
 * A lily pad is a background decoration that appears near water.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class LilyPad extends WorldSprite {
    private static final GreenfootImage[] LILY_PAD_VARIATIONS = {new GreenfootImage("lily_pad1.png"), new GreenfootImage("lily_pad2.png"), new GreenfootImage("lily_pad3.png"),
        new GreenfootImage("lily_pad4.png")};

    public LilyPad(int lilyPadType) {
        super(Layer.TOPGROUND);
        setOriginalImage(LILY_PAD_VARIATIONS[lilyPadType]);
    }
}
