import greenfoot.*;
import java.util.List;
import java.util.Comparator;

public class SpriteStackingWorld extends PixelWorld {
    private static final int OBJECT_SPAWN_RANGE = 2000;

    static {
        SprackView.loadAll();
    }

    public SpriteStackingWorld() {
        super(256, 196);

        Camera.resetTo(0, 0, 0, 3);
        Camera.setCloseness(0.3);

        addSprack(new Player(), 0, 0);
        addSprack(new Sprack("tower"), 0, -100);
        addSprack(new Sprack("monu3"), 100, 0);
        for (int i = 0; i < 100; i++) {
            addSprack(new Sprack("crate"), Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2, Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2);
        }
        for (int i = 0; i < 30; i++) {
            addSprack(new Sprack("log"), Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2, Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2);
        }
        // for (int i = 0; i < 100; i++) {
        //     addSprack(new Sprack("tree"), Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2, Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2);
        // }

        render();
    }

    @Override
    public void update() {
        if (Greenfoot.isKeyDown("e")) {
            Camera.setZoom(Camera.getZoom() * 1.01);
        }
        if (Greenfoot.isKeyDown("q")) {
            Camera.setZoom(Camera.getZoom() * 0.99);
        }

        List<? extends Sprite> spracks = getSpritesByLayer(Layer.SPRACK_DEFAULT);
        spracks.sort(Comparator.comparing(Sprite::getScreenY));

        updateSprites();
        Timer.incrementAct();
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
