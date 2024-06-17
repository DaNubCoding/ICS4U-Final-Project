import greenfoot.*;
import java.util.List;
import java.util.Random;
import java.util.ArrayList;

/**
 * A medium-sized body of water.
 *
 * @author Sandra Huang
 * @version June 2024
 */
public class Pond extends WorldSprite
{
    private static final GreenfootImage[] POND_VARIATIONS = {new GreenfootImage("pond1.png"),
        new GreenfootImage("pond2.png"), new GreenfootImage("pond3.png")};

    private Random rand;
    private List<Cattail> cattails;
    private List<LilyPad> lilyPads;
    private int numCattails;
    private int numLilyPads;

    /**
     * Make a pond with a defined seeded Random.
     */
    public Pond(Random rand, int pondType, int rotation){
        super(Layer.GROUND);
        setOriginalImage(POND_VARIATIONS[pondType]);
        setWorldRotation(rotation);
        this.rand = rand;

        cattails = new ArrayList<>();
        numCattails = rand.nextInt(5, 14);
        for(int i=0; i<numCattails; i++){
            cattails.add(new Cattail(rand.nextInt(4)));
        }
        lilyPads = new ArrayList<>();
        numLilyPads = rand.nextInt(3, 8);
        for(int i=0; i<numLilyPads; i++){
            lilyPads.add(new LilyPad(rand.nextInt(4)));
        }
    }

    @Override
    public void addedToWorld(PixelWorld world){
        SprackWorld w = (SprackWorld) world;
        int size = getSize();
        for(Cattail cattail : cattails){
            w.addWorldObject(cattail, getWorldX() + rand.nextInt(size) - size / 2, getWorldY(), getWorldZ() + rand.nextInt(size) - size / 2);
        }
        for(LilyPad lilipad : lilyPads){
            w.addWorldObject(lilipad, getWorldX() + rand.nextInt((int) (size * 0.6)) - size * 0.3, getWorldY(), getWorldZ() + rand.nextInt((int) (size * 0.6)) - size * 0.3);
        }
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

    @Override
    public void removedFromWorld(PixelWorld world){
        for(Cattail cattail : cattails){
            world.removeSprite(cattail);
        }
        for(LilyPad lilyPad : lilyPads){
            world.removeSprite(lilyPad);
        }
    }
}
