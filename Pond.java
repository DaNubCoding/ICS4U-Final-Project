import greenfoot.*;
/**
 * A medium-sized body of water
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Pond extends WorldSprite
{
    private static final GreenfootImage[] POND_VARIATIONS = {new GreenfootImage("pond1.png"),
        new GreenfootImage("pond2.png"), new GreenfootImage("pond3.png")};

    /**
     * Makes a pond
     */
    public Pond(int pondType, int rotation){
        super(Layer.GROUND);
        setOriginalImage(POND_VARIATIONS[pondType]);
        setWorldRotation(rotation);
    }

    /**
     * Get the approximate size of the pond by taking the average of the width
     * and height of the image.
     *
     * @return the approximate size of the pond
     */
    public int getSize() {
        return (getOriginalImage().getWidth() + getOriginalImage().getHeight()) / 2;
    }
}
