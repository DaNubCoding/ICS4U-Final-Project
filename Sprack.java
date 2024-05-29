import greenfoot.*;

public class Sprack extends Sprite {
    private final String sheetName;

    private Vector2 worldPos;
    private double rotation;

    public Sprack(String sheetName) {
        super(Layer.SPRACK_DEFAULT);
        /*
        view = SprackView.getView(sheetName);
        */
        this.sheetName = sheetName;
        worldPos = new Vector2();
    }

    public void setSpriteRotation(double rotation) {
        this.rotation = Vector2.normalizeAngle(rotation);
    }

    public double getSpriteRotation() {
        return rotation;
    }

    public void setWorldPos(double x, double y) {
        worldPos = new Vector2(x, y);
    }

    public void setWorldPos(Vector2 position) {
        worldPos = position;
    }

    @Override
    public void render(GreenfootImage canvas) {
        SprackView view = SprackView.getView(sheetName);
        if (view == null) {
            return;
        }

        // Update screen position, rotated around zoomed camera position
        double scale = Camera.getZoom();
        double offsetX = (worldPos.x - Camera.getX()) * scale;
        double offsetY = (worldPos.y - Camera.getY()) * scale;
        double screenRad = Math.toRadians(-Camera.getRotation());
        double screenX = getWorld().getWidth() / 2 + offsetX * Math.cos(screenRad) - offsetY * Math.sin(screenRad);
        double screenY = getWorld().getHeight() / 2 + offsetX * Math.sin(screenRad) + offsetY * Math.cos(screenRad);
        setScreenPos(screenX, screenY);

        // Don't render if offscreen
        double imageRotation = rotation - Camera.getRotation();
        int centerX = view.getCenterX(imageRotation, scale);
        int centerY = view.getCenterY(imageRotation, scale);
        if (screenX + centerX < 0
            || screenX - centerX >= getWorld().getWidth()
            || screenY + (view.getTransformedHeight(imageRotation, scale) - centerY) < 0
            || screenY - centerY >= getWorld().getHeight()) {
            return;
        }

        // Draw image, screen position at center of bottom layer
        GreenfootImage image = view.getTransformedImage(imageRotation, Camera.getZoom());
        if (image == null) {
            return;
        }
        canvas.drawImage(image, (int) screenX - centerX, (int) screenY - centerY);
    }

    public double getWorldX() {
        return worldPos.x;
    }

    public double getWorldY() {
        return worldPos.y;
    }

    public Vector2 getWorldPos() {
        return worldPos;
    }
}
