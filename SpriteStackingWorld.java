import greenfoot.*;
import java.util.List;
import java.util.Comparator;

public class SpriteStackingWorld extends PixelWorld {
    private static final int OBJECT_SPAWN_RANGE = 2000;
    private WorldData wd;

    static {
        SprackView.loadAll();
    }

    public SpriteStackingWorld() {
        super(256, 196);

        Camera.resetTo(0, 0, 0, 3);
        Camera.setCloseness(0.3);
        wd = new WorldData();

        wd.generateWorld();
        for(Vector2 v : wd.getSurroundings().keySet()){
            if(wd.getSurroundings().get(v)!=null){
                addSprack(wd.getSurroundings().get(v), (int)v.x*20, (int)v.y*20);
            }
        }
        addSprack(new Player(), 0, 0);
        // addSprack(new Sprack("tower"), 0, -100);
        // addSprack(new Sprack("monu3"), 100, 0);
        // for (int i = 0; i < 1000; i++) {
        //     addSprack(new Sprack("crate"), (
        //         Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE / 20) - OBJECT_SPAWN_RANGE / 20 / 2) * 20,
        //         (Greenfoot.getRandomNumber(OBJECT_SPAWN_RANGE / 20) - OBJECT_SPAWN_RANGE / 20 / 2) * 20
        //     );
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

        if(wd.updatePlayerLocation((int)(Camera.getX()/20), (int)(Camera.getY()/20))){
            List<? extends Sprite> sprites = getSprites(WorldElement.class);
            // remove objects not present in world data
            for(Sprite s : sprites){
                WorldElement w = (WorldElement) s;
                if(!wd.getSurroundings().containsValue(w)){
                    removeSprite(w);
                }
            }
            // add objects not already in world
            for(Vector2 v : wd.getSurroundings().keySet()){
                if(wd.getSurroundings().get(v)!=null 
                && !sprites.contains(wd.getSurroundings().get(v))){
                    addSprack(wd.getSurroundings().get(v), (int)(v.x*20), (int)(v.y*20));
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
