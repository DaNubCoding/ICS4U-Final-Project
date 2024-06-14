import greenfoot.*;

public class TitleWorld extends PixelWorld {
    private GifSprite panorama = new GifSprite(new GifImage("panorama.gif"), Layer.UI);

    public TitleWorld() {
        super(256, 196);
        addSprite(panorama, getWidth() / 2, getHeight() / 2);
    }

    @Override
    public void update() {
        if (Greenfoot.isKeyDown("space")) {
            Greenfoot.setWorld(new WorldSelection());
        }

        panorama.updateImage();
        updateSprites();
        Timer.incrementAct();
    }

    @Override
    public void render() {
        renderSprites();
    }
}
