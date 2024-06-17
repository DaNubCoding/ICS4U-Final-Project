import greenfoot.*;

/**
 * The TitleWorld is the first world the player sees. It displays the title.
 *
 * @author Andrew Wang
 * @author Martin Baldwin
 * @version June 2024
 */
public class TitleWorld extends PixelWorld {
    private GifSprite panorama = new GifSprite(new GifImage("panorama.gif"), Layer.GROUND);
    private static GreenfootImage title = new GreenfootImage("title.png");

    public TitleWorld() {
        super(256, 196);
        addSprite(panorama, getWidth() / 2, getHeight() / 2);
        addSprite(new Text("Press any button", Text.AnchorX.CENTER, Text.AnchorY.BOTTOM, new Color(227, 250, 175, 128)), getWidth() / 2, getHeight() - 30);
        Music.set("title_music.wav");

        applyAdditions();
        render();
        updateImage();

        // clear previous keys
        Greenfoot.getKey();
    }

    @Override
    public void update() {
        if (Greenfoot.getKey() != null || Greenfoot.mouseClicked(null)) {
            Greenfoot.setWorld(new SelectionWorld(panorama));
            return;
        }

        panorama.updateImage();
        updateSprites();
        Timer.incrementAct();
    }

    @Override
    public void started() {
        super.started();
        // Clear any previous key before checking in update()
        Greenfoot.getKey();
    }

    @Override
    public void render() {
        GreenfootImage canvas = getCanvas();
        renderSprites();
        canvas.setColor(new Color(0, 0, 0, 35));
        canvas.fill();
        canvas.drawImage(title, getWidth() / 2 - title.getWidth() / 2, 30);
    }
}
