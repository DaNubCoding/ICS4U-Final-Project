import greenfoot.*;

/**
 * An item to display helpful text to the player so they can find out how to
 * play the game.
 *
 * @author Martin Baldwin
 * @version June 2024
 */
public class Manual extends Item {
    private final Text page = new Text(
        "Turn - Point mouse\n"
        + "Move around - WASD\n"
        + "Dash - Space\n"
        + "Drop item - Q\n"
        + "Change item - Tab\n"
        + "Use item - Left Click\n"
        + "Move camera - Right Click Drag\n"
        + "\n"
        + "Walk up to crates to open them and collect new items! Explore the world and find out what lies out there...",
        Text.AnchorX.CENTER, Text.AnchorY.CENTER, SprackWorld.WORLD_WIDTH - 64, new Color(238, 223, 200, 224)
    );

    private boolean isOpen = false;

    public Manual() {
        super("manual.png");
    }

    @Override
    public void removedFromWorld(PixelWorld world) {
        super.removedFromWorld(world);
        world.removeSprite(page);
        isOpen = false;
    }

    @Override
    public void drop() {
        super.drop();
        getWorld().removeSprite(page);
    }

    @Override
    public void update() {
        super.update();
        if (isOnGround()) {
            return;
        }

        MouseInfo mouseInfo = Greenfoot.getMouseInfo();
        if (Greenfoot.mouseClicked(null) && mouseInfo != null && mouseInfo.getButton() == 1) {
            isOpen = !isOpen;
            if (isOpen) {
                getWorld().addSprite(page, getWorld().getWidth() / 2, (getWorld().getHeight() - 42) / 2);
            } else {
                getWorld().removeSprite(page);
            }
        }
    }

    @Override
    public String getProperName() {
        return "Manual (Click to Open!)";
    }
}
