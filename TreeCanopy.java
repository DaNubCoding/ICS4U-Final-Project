import greenfoot.*;

/**
 * The canopy of a tree.
 *
 * @author Andrew Wang
 * @version May 2024
 */
public class TreeCanopy extends Sprack {
    public TreeCanopy(String sheetName) {
        super(sheetName, Layer.SPRACK_CANOPY);
        setWorldRotation(Greenfoot.getRandomNumber(360));
    }
}
