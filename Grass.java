import greenfoot.GreenfootImage;
/**
 * A great source of fibre!
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Grass extends WorldSprite
{
    private static final GreenfootImage[] GRASS_VARIATIONS = {new GreenfootImage("grass1.png"), new GreenfootImage("grass2.png"), new GreenfootImage("grass3.png"),
        new GreenfootImage("grass4.png")};

    public Grass(int grassType){
        setOriginalImage(GRASS_VARIATIONS[grassType]);
    }

    public void update(){
        setWorldRotation(Camera.getRotation());
    }
}
