import java.io.File;

import greenfoot.Color;
import greenfoot.Greenfoot;
import greenfoot.GreenfootImage;

/**
 * The screen where all of the saved worlds are displayed.
 *
 * @author Lucas Fu
 * @version (a version number or a date)
 */
public class WorldSelection extends PixelWorld {
    private static long[] worldSeeds;
    private static int topIndex = 0;
    private Button button1;
    private Button button2;
    private Button button3;

    public WorldSelection() {
        super(SprackWorld.WORLD_WIDTH, SprackWorld.WORLD_HEIGHT);
        File path = new File("saves");
        if (!path.exists()) {
            path.mkdirs();
        }
        String[] worldNames = path.list();
        worldSeeds = new long[worldNames.length];
        for (int i = 0; i < worldNames.length; i++) {
            worldSeeds[i] = Long.valueOf(worldNames[i].substring(5, worldNames[i].length() - 4));
        }
        button1 = new Button("aaaaaaaaaaaaaaaaaaaaa", WorldSelection::openWorld1);
        button2 = new Button("aaaaaaaaaaaaaaaaaaaaa", WorldSelection::openWorld2);
        button3 = new Button("aaaaaaaaaaaaaaaaaaaaa", WorldSelection::openWorld3);

        if (worldSeeds.length > 0) {
            addSprite(button1, 100, 35);
            addSprite(button2, 100, 65);
            addSprite(button3, 100, 95);
        }

        addSprite(new Button("Generate Random World", WorldSelection::makeRandomWorld), 100, 135);
        addSprite(new Button("scroll up", WorldSelection::scrollUp), 200, 50);
        addSprite(new Button("scroll down", WorldSelection::scrollDown), 200, 80);

        applyAdditions();
        render();
        updateImage();
    }

    private static void openWorld(boolean seeded, long seed) {
        Greenfoot.setWorld(new LoadingWorld(new SprackWorld(seeded, seed)));
    }

    private static void openWorld1() {
        openWorld(false, worldSeeds[topIndex]);
    }

    private static void openWorld2() {
        int index = (topIndex + 1) % worldSeeds.length;
        openWorld(false, worldSeeds[index]);
    }

    private static void openWorld3() {
        int index = (topIndex + 2) % worldSeeds.length;
        openWorld(false, worldSeeds[index]);
    }

    private static void makeRandomWorld() {
        openWorld(true, 0);
    }

    private static void scrollUp() {
        topIndex = ((topIndex - 1) + worldSeeds.length) % worldSeeds.length;
    }

    private static void scrollDown() {
        topIndex = (topIndex + 1) % worldSeeds.length;
    }

    @Override
    public void update() {
        updateSprites();
    }

    @Override
    public void render() {
        GreenfootImage background = getCanvas();
        background.setColor(Color.GRAY);
        background.fill();

        if (worldSeeds.length > 0) {
            button1.setText("World with seed: " + String.valueOf(worldSeeds[topIndex]));
            button2.setText("World with seed: " + String.valueOf(worldSeeds[(topIndex + 1) % worldSeeds.length]));
            button3.setText("World with seed: " + String.valueOf(worldSeeds[(topIndex + 2) % worldSeeds.length]));
        }
        renderSprites();
    }
}
