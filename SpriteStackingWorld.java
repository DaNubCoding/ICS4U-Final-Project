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
        Camera.setCloseness(0.2);

        addSprite(new Player(), 0, 0);
        for (int i = 0; i < 100; i++) {
            Sprack sprack = new Sprack("crate");
            addSprite(sprack, 0, 0);
            sprack.setWorldLocation(Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2, Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2);
        }
        /*
        for (int i = 0; i < 100; i++) {
            addObject(new Sprack("building"), Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2, Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2);
        }
        for (int i = 0; i < 100; i++) {
            addObject(new Sprack("tree"), Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2, Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE) - OBJECT_SPAWN_RANGE / 2);
        }
        */
        addSprite(new Sprack("tower"), 0, 0);

        render();
    }

    @Override
    public void update() {
        if (Greenfoot.isKeyDown("w")) {
            Camera.setZoom(Camera.getZoom() * 1.01);
        }
        if (Greenfoot.isKeyDown("s")) {
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
}
