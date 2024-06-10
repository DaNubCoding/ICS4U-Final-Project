import greenfoot.*;

/**
 * A health bar that displays the health of an entity right above it. This will
 * automatically follow the entity and update the health.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public class HealthBar extends WorldSprite {
    private Entity owner;
    private double maxHealth;
    private boolean maxHealthSet;

    /**
     * Create a new health bar for the given entity.
     */
    public HealthBar(Entity owner) {
        super(Layer.HEALTH_BAR);
        this.owner = owner;
    }

    @Override
    public void update() {
        setWorldPos(owner.getWorldPos().add(new Vector3(0, owner.getHeight() + 10, 0)));
        setWorldRotation(Camera.getRotation());
    }

    /**
     * Set the health of this health bar, which updates the health bar's image.
     */
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
