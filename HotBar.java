import java.util.ArrayList;
import greenfoot.*;

/**
 * The hotbar that displays the player's items.
 *
 * @author Matthew Li
 * @author Andrew Wang
 * @version June 2024
 */
public class HotBar extends Sprite {
    private static final int HOTBAR_HEIGHT = 30;
    private static final int MARGIN = 5;

    public HotBar() {
        super(Layer.UI);
    }

    @Override
    public void render(GreenfootImage canvas) {
        SprackWorld world = (SprackWorld) getWorld();
        ArrayList<Item> playerItems = world.getPlayer().getHotbar();
        int width = MARGIN;

        for (Item item : playerItems) {
            width += item.getOriginalImage().getWidth() + MARGIN;
        }

        GreenfootImage hotbar = new GreenfootImage(width, HOTBAR_HEIGHT);
        hotbar.setColor(new Color(192, 192, 192, 128));
        hotbar.fill();

        int startX = (world.getWidth() - width) / 2;

        int y = world.getHeight() - HOTBAR_HEIGHT;
        canvas.drawImage(hotbar, startX, y);

        int x = startX + MARGIN;

        for (Item item : playerItems) {
            canvas.drawImage(item.getOriginalImage(), x, y + (HOTBAR_HEIGHT - item.getOriginalImage().getHeight()) / 2);
            x += item.getOriginalImage().getWidth() + MARGIN;
        }
    }
}
