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
        "Move around - WASD\n"
        + "Dash - Space\n"
        + "Leap - E\n"
        + "Drop item - Q\n"
        + "Change item - Tab\n"
        + "Use item - Left Click\n"
        + "Move camera - Right Click Drag\n"
        + "\n"
        + "Walk up to crates to open them and collect new items!\n"
        + "\n"
        + "When you're ready, press Q. Explore the world and find out what lies out there...",
        Text.AnchorX.CENTER, Text.AnchorY.CENTER, SprackWorld.WORLD_WIDTH - 64, new Color(238, 223, 200, 224)
    );

    public Manual() {
        super("manual.png");
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        super.addedToWorld(world);
        showPage();
    }

    @Override
    public void removedFromWorld(PixelWorld world) {
        super.removedFromWorld(world);
        world.removeSprite(page);
    }

    private void showPage() {
        getWorld().addSprite(page, getWorld().getWidth() / 2, (getWorld().getHeight() - 42) / 2);
    }

    @Override
    public void update() {
        super.update();
        if (isOnGround() && !page.isRemoved()) {
        getWorld().removeSprite(page);
        } else if (!isOnGround() && page.isRemoved()) {
            showPage();
        }
    }
}
