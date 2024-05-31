import greenfoot.*;

import java.util.List;
import java.util.Comparator;

public class SpriteStackingWorld extends PixelWorld {
    // private static final int OBJECT_SPAWN_RANGE = 2000;
    private WorldData worldData;

    static {
        SprackView.loadAll();
    }

    public SpriteStackingWorld() {
        super(256, 196);

        Camera.resetTo(0, 0, 0, 3);
        Camera.setCloseness(0.3);
        worldData = new WorldData();

        worldData.generateWorld();
        for(Vector2 coord : worldData.getSurroundings().keySet()) {
            if(worldData.getSurroundings().get(coord)!=null) {
                final int x = (int) coord.x * 20, y = (int) coord.y * 20;
                addSprack(worldData.getSurroundings().get(coord), x, y);
            }
        }
        addSprack(new Player(), 0, 0);

        render();
    }

    @Override
    public void update() {
        List<? extends Sprite> spracks = getSpritesByLayer(Layer.SPRACK_DEFAULT);
        spracks.sort(Comparator.comparing(Sprite::getScreenY));

        updateSprites();
        Timer.incrementAct();

        int cameraGridX = (int) (Camera.getX() / 20);
        int cameraGridY = (int) (Camera.getY() / 20);
        if(worldData.updatePlayerLocation(cameraGridX, cameraGridY)) {
            List<? extends Sprite> features = getSprites(Feature.class);
            // remove objects not present in world data
            for(Sprite sprite : features) {
                Feature feature = (Feature) sprite;
                if(!worldData.getSurroundings().containsValue(feature)) {
                    removeSprite(feature);
                }
            }
            // add objects not already in world
            for(Vector2 coord : worldData.getSurroundings().keySet()) {
                if(worldData.getSurroundings().get(coord) != null
                && !features.contains(worldData.getSurroundings().get(coord))) {
                    final int x = (int) coord.x * 20, y = (int) coord.y * 20;
                    addSprack(worldData.getSurroundings().get(coord), x, y);
                }
            }
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
     */
    public void addSprack(Sprack sprack, int x, int y) {
        addSprite(sprack, x, y);
        sprack.setWorldPos(x, y);
    }
}
