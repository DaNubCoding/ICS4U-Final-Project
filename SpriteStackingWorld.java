import greenfoot.*;

import java.util.List;
import java.util.Comparator;

public class SpriteStackingWorld extends PixelWorld {
    // private static final int OBJECT_SPAWN_RANGE = 2000;
    private WorldData worldData;
    private Player player;

    // world information
    public static final int WORLD_WIDTH = 256;
    public static final int WORLD_HEIGHT = 196;

    static {
        SprackView.loadAll();
    }

    public SpriteStackingWorld() {
        super(WORLD_WIDTH, WORLD_HEIGHT);

        worldData = new WorldData(1);

        Vector2 playerPos = worldData.getPlayerLocation();
        player = new Player();
        addSprack(player, playerPos.x * 20, 0, playerPos.y * 20);

        Camera.resetTo(playerPos.x, 0, playerPos.y, 0, 1.5);
        Camera.setCloseness(0.3);

        worldData.generateWorld();
        for (Vector2 coord : worldData.getSurroundings().keySet()) {
            if (worldData.getSurroundings().get(coord) != null) {
                final int x = (int) coord.x * 20, z = (int) coord.y * 20;
                addSprack(worldData.getSurroundings().get(coord), x, 0, z);
            }
        }

        render();
    }

    @Override
    public void update() {
        List<? extends Sprite> spracks = getSpritesByLayer(Layer.SPRACK_DEFAULT);
        spracks.sort(Comparator.comparing(Sprite::getScreenY));
        spracks = getSpritesByLayer(Layer.SPRACK_CANOPY);
        spracks.sort(Comparator.comparing(Sprite::getScreenY));

        updateSprites();
        Timer.incrementAct();

        int cameraGridX = (int) (Camera.getX() / 20);
        int cameraGridZ = (int) (Camera.getZ() / 20);
        if (worldData.updatePlayerLocation(cameraGridX, cameraGridZ)) {
            List<? extends Sprite> features = getSprites(Feature.class);
            // remove objects not present in world data
            for (Sprite sprite : features) {
                Feature feature = (Feature) sprite;
                if (!worldData.getSurroundings().containsValue(feature)) {
                    removeSprite(feature);
                }
            }
            // add objects not already in world
            for (Vector2 coord : worldData.getSurroundings().keySet()) {
                if (worldData.getSurroundings().get(coord) != null
                && !features.contains(worldData.getSurroundings().get(coord))) {
                    final int x = (int) coord.x * 20, z = (int) coord.y * 20;
                    addSprack(worldData.getSurroundings().get(coord), x, 0, z);
                }
            }
        }

        if (Greenfoot.isKeyDown("enter")) {
            worldData.saveData();
        }

        if (Greenfoot.isKeyDown("m")) {
            Greenfoot.setWorld(new WorldMap(this, worldData));
        }
    }

    @Override
    public void render() {
        GreenfootImage background = getCanvas();
        background.setColor(new Color(56, 56, 56));
        background.fill();
        renderSprites();
    }

    /**
     * Add a Sprack to the world at the given world position.
     * <p>
     * This is equivalent to calling {@link #addSprite(Sprite, int, int)}
     * followed by {@link Sprack#setWorldPos(double, double)}.
     *
     * @param sprack the Sprack to add
     * @param x the x position
     * @param y the y position
     * @param z the z position
     */
    public void addSprack(Sprack sprack, double x, double y, double z) {
        addSprite(sprack, 0, 0);
        sprack.setWorldPos(x, y, z);
    }

    /**
     * Get the player object.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Get the world data object.
     *
     * @return the world data
     */
    public WorldData getWorldData() {
        return worldData;
    }
}
