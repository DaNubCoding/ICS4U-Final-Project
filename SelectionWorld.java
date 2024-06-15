import java.io.File;

import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

/**
 * The screen where all of the saved worlds are displayed. The player can open
 * an existing world or create a new world to play in.
 *
 * @author Lucas Fu
 * @author Martin Baldwin
 * @version June 2024
 */
public class SelectionWorld extends PixelWorld {
    private static final Color WORLD_BOX_COLOR = new Color(0, 0, 0, 100);

    private long[] worldSeeds;
    private int topIndex = 0;
    private Button[] buttons;
    private GifSprite panorama;

    public SelectionWorld(GifSprite panorama) {
        super(SprackWorld.WORLD_WIDTH, SprackWorld.WORLD_HEIGHT);
        File path = new File("saves");
        // Create saves directory if it doesn't exist
        if (!path.exists()) {
            path.mkdirs();
        }
        // Get all existing save files
        String[] worldNames = path.list();
        worldSeeds = new long[worldNames.length];
        for (int i = 0; i < worldNames.length; i++) {
            worldSeeds[i] = Long.valueOf(worldNames[i].substring(5, worldNames[i].length() - 4));
        }
        // Create buttons to allow choosing an existing world to open
        buttons = new Button[Math.min(3, worldSeeds.length)];
        for (int i = 0; i < buttons.length; i++) {
            final int buttonIndex = i;
            buttons[i] = new Button("a", () -> {
                int index = (topIndex + buttonIndex) % worldSeeds.length;
                openWorld(false, worldSeeds[index]);
            }, 164, -1);
            addSprite(buttons[i], 104, 89 + 28 * i);
        }
        updateButtonText();

        this.panorama = panorama;
        addSprite(new Button("Generate Random World", SelectionWorld::openNewRandomWorld), 104, 45);
        addSprite(new Button("up", this::scrollUp, 40, -1), 222, 101);
        addSprite(new Button("down", this::scrollDown, 40, -1), 222, 129);

        applyAdditions();
        render();
        updateImage();

        Music.set("title_music.wav");
    }

    /**
     * Update the text of all buttons corresponding to the indices of the worlds
     * that they will open.
     */
    private void updateButtonText() {
        for (int i = 0; i < buttons.length; i++) {
            buttons[i].setText("World " + worldSeeds[(topIndex + i) % worldSeeds.length]);
        }
    }

    /**
     * Open a world with the given seed parameters.
     * <p>
     * If all SprackView caches have been created, the scenario will switch
     * directly to a SprackWorld. Otherwise, a LoadingWorld will be shown before
     * the SprackWorld.
     *
     * @see SprackWorld
     */
    private static void openWorld(boolean seeded, long seed) {
        SprackWorld world = new SprackWorld(seeded, seed);
        if (SprackView.loaded()) {
            Greenfoot.setWorld(world);
        } else {
            Greenfoot.setWorld(new LoadingWorld(world));
        }
    }

    private static void openNewRandomWorld() {
        openWorld(true, 0);
    }

    private void scrollUp() {
        if (worldSeeds.length == 0) {
            return;
        }
        topIndex = ((topIndex - 1) + worldSeeds.length) % worldSeeds.length;
        updateButtonText();
    }

    private void scrollDown() {
        if (worldSeeds.length == 0) {
            return;
        }
        topIndex = (topIndex + 1) % worldSeeds.length;
        updateButtonText();
    }

    @Override
    public void update() {
        panorama.updateImage();
        updateSprites();

        Timer.incrementAct();
    }

    @Override
    public void render() {
        GreenfootImage canvas = getCanvas();
        panorama.render(canvas);
        canvas.setColor(new Color(0, 0, 0, 80));
        canvas.fill();
        // Draw backgrounds behind world buttons
        canvas.setColor(WORLD_BOX_COLOR);
        canvas.fillRect(14, 27, 180, 36);
        canvas.fillRect(14, 71, 180, 92);
        renderSprites();
    }
}
