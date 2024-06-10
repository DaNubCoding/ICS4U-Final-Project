import greenfoot.*;

public class HealthBar extends WorldSprite {
    private Entity owner;
    private double maxHealth;
    private boolean maxHealthSet;

    public HealthBar(Entity owner) {
        super(Layer.HEALTH_BAR);
        this.owner = owner;
    }

    @Override
    public void update() {
        setWorldPos(owner.getWorldPos().add(new Vector3(0, owner.getHeight() + 10, 0)));
        setWorldRotation(Camera.getRotation());
    }

    public void setHealth(double health) {
        if (!maxHealthSet) {
            maxHealth = health;
            maxHealthSet = true;
        }

        GreenfootImage image = new GreenfootImage(30, 4);
        image.setColor(Color.GRAY);
        image.fill();
        image.setColor(Color.RED);
        image.fillRect(0, 0, (int) (health / maxHealth * 30), 4);
        setOriginalImage(image);
    }
}
