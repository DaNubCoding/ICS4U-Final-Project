import greenfoot.GreenfootImage;
import java.util.List;

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

    @Override
    public void update(){
        setWorldRotation(Camera.getRotation());

        List<? extends Sprite> ponds = getWorld().getSprites(Pond.class);
        for (Sprite s : ponds){
            Pond pond = (Pond) s;
            if (pond.getWorldPos().distanceTo(getWorldPos()) < pond.getSize() / 2){
                getWorld().removeSprite(this);
                break;
            }
        }
    }
}
