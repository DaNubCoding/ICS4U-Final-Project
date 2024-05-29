import greenfoot.*;

public abstract class Sprite {
    private Layer layer;
    private Vector2 position;
    private GreenfootImage image;
    private PixelWorld world;

    public Sprite(Layer layer) {
        position = new Vector2();
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

    public void setScreenLocation(double x, double y) {
        position = new Vector2(x, y);
    }

    public void setScreenLocation(Vector2 position) {
        this.position = position;
    }

    public Vector2 getScreenLocation() {
        return position;
    }

    public double getScreenX() {
        return position.x;
    }

    public double getScreenY() {
        return position.y;
    }

    public void render(GreenfootImage canvas) {
        int x = (int) position.x - image.getWidth() / 2;
        int y = (int) position.y - image.getHeight() / 2;
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
        return position.distanceTo(x, y);
    }
}
