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
        for (int i = 0; i < 1000; i++) {
            addSprack(new Sprack("crate"), (
                Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE / 20) - OBJECT_SPAWN_RANGE / 20 / 2) * 20,
                (Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE / 20) - OBJECT_SPAWN_RANGE / 20 / 2) * 20
            );
        }

        render();
    }

    @Override
    public void update() {
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
