import greenfoot.*;
/**
 * A small patch of brown dirt.
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Dirt extends WorldSprite
{
    private static final GreenfootImage[] DIRT_VARIATIONS = {new GreenfootImage("dirt1.png"),
        new GreenfootImage("dirt2.png"), new GreenfootImage("dirt3.png"), new GreenfootImage("dirt4.png"), new GreenfootImage("dirt5.png")};

    /**
     * Makes a dirt
     */
    public Dirt(int dirtType, int rotation){
        super(Layer.GROUND);
        setOriginalImage(DIRT_VARIATIONS[dirtType]);
        setWorldRotation(rotation);
    }
}
