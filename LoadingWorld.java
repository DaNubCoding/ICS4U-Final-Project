import greenfoot.*;

/**
 * The world that is displayed as SprackViews are cached...
 *
 * @author Martin Baldwin
 * @version June 2024
 */
public class LoadingWorld extends PixelWorld {
    private static final Color BACKGROUND_COLOR = new Color(158, 158, 158);
    private static final Color PROGRESS_COLOR = new Color(255, 90, 90);
    private static final Color NONPROGRESS_COLOR = new Color(190, 190, 190);
    private static final Color BORDER_COLOR = Color.BLACK;

    private static final int BAR_HEIGHT = 20;

    private final SprackWorld nextWorld;

    private String loadingString = "Loading...";
    private final Text loadingText;
    private final Timer loadingTimer;

    public LoadingWorld(SprackWorld nextWorld) {
        super(nextWorld.getWidth(), nextWorld.getHeight());
        this.nextWorld = nextWorld;
        loadingText = new Text(loadingString, Text.AnchorX.LEFT, Text.AnchorY.BOTTOM);
        addSprite(loadingText, 20, (getHeight() - BAR_HEIGHT) / 2 - 3);
        loadingTimer = new Timer(30);

        if (!SprackView.loaded()) {
            new Thread(SprackView::loadAll).start();
        }

        applyAdditions();
        render();
        updateImage();
    }

    @Override
    public void update() {
        if (SprackView.loaded()) {
            Greenfoot.setWorld(nextWorld);
            return;
        }

        if (loadingTimer.ended()) {
            loadingString += ".";
            loadingText.setContent(loadingString);
            loadingTimer.restart();
        }
        updateSprites();

        Timer.incrementAct();
    }

    public void render() {
        GreenfootImage canvas = getCanvas();
        canvas.setColor(BACKGROUND_COLOR);
        canvas.fill();

        canvas.setColor(NONPROGRESS_COLOR);
        canvas.fillRect(20, (getHeight() - BAR_HEIGHT) / 2, getWidth() - 40, BAR_HEIGHT);

        double progress = SprackView.getLoadProgress();
        canvas.setColor(PROGRESS_COLOR);
        canvas.fillRect(20, (getHeight() - BAR_HEIGHT) / 2, (int) (progress * (getWidth() - 40)), BAR_HEIGHT);

        canvas.setColor(BORDER_COLOR);
        canvas.drawRect(20, (getHeight() - BAR_HEIGHT) / 2, getWidth() - 40, BAR_HEIGHT);

        renderSprites();
        updateImage();
    }
}
