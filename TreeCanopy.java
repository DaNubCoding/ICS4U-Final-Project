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

    @Override
    public void update() {
        Vector2 playerPos = getWorld().getPlayer().getScreenPos();
        Vector2 canopyPos = getScreenPos();
        double distance = playerPos.distanceTo(canopyPos) / Camera.getZoom();
        setTransparency((int) Math.min(255 - (1 - (distance / 60.0)) * 180, 255));
    }
}
