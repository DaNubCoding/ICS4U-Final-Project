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
    private static final int MARGIN = 4;

    public HotBar() {
        super(Layer.UI);
    }

    @Override
    public void render(GreenfootImage canvas) {
        SprackWorld world = (SprackWorld) getWorld();
        ArrayList<Item> playerItems = world.getPlayer().getHotbar();
        int width = 0;

        for (Item item : playerItems) {
            width += item.getOriginalImage().getWidth() + MARGIN * 2;
        }

        if (width == 0) return;

        GreenfootImage hotbar = new GreenfootImage(width, HOTBAR_HEIGHT);
        hotbar.setColor(new Color(192, 192, 192, 128));
        hotbar.fill();

        int startX = (world.getWidth() - width) / 2;

        int y = world.getHeight() - HOTBAR_HEIGHT;
        canvas.drawImage(hotbar, startX, y);

        int x = startX;

        for (Item item : playerItems) {
            x += MARGIN;
            GreenfootImage image = item.getOriginalImage();
            canvas.drawImage(image, x, y + (HOTBAR_HEIGHT - image.getHeight()) / 2);
            if (item.getWorld() != null) {
                canvas.setColor(Color.WHITE);
                canvas.drawRect(x - MARGIN, y, image.getWidth() + MARGIN * 2, HOTBAR_HEIGHT - 1);
            }
            x += image.getWidth() + MARGIN;
        }
    }
}
