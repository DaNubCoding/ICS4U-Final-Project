import greenfoot.*;

public abstract class Sprite {
    private Layer layer;
    private Vector2 screenPos;
    private GreenfootImage image;
    private PixelWorld world;

    public Sprite(Layer layer) {
        screenPos = new Vector2();
        image = null;
        world = null;
        this.layer = layer;
    }

    public Layer getLayer() {
        return layer;
    }

    public void setImage(GreenfootImage image) {
        this.image = new GreenfootImage(image);
    }

    public GreenfootImage getImage() {
        return image;
    }

    public void setScreenPos(double x, double y) {
        screenPos = new Vector2(x, y);
    }

    public void setScreenPos(Vector2 position) {
        this.screenPos = position;
    }

    public Vector2 getScreenPos() {
        return screenPos;
    }

    public double getScreenX() {
        return screenPos.x;
    }

    public double getScreenY() {
        return screenPos.y;
    }

    public void render(GreenfootImage canvas) {
        int x = (int) screenPos.x - image.getWidth() / 2;
        int y = (int) screenPos.y - image.getHeight() / 2;
        canvas.drawImage(image, x, y);
    }

    public void update() {}

    public void setWorld(PixelWorld world) {
        this.world = world;
    }

    public PixelWorld getWorld() {
        return world;
    }

    public void addedToWorld(PixelWorld world) {}

    public double distanceTo(double x, double y) {
        return screenPos.distanceTo(x, y);
    }
}
