import greenfoot.*;

/**
 * A health bar that displays the health of an entity right above it. This will
 * automatically follow the entity and update the health.
 * <p>
 * Use the modification methods to change how this bar looks!
 *
 * @author Andrew Wang
 * @author Lucas Fu
 * @author Martin Baldwin
 * @version June 2024
 */
public class HealthBar extends WorldSprite {
    private Entity owner;
    private double maxHealth;
    private boolean maxHealthSet;
    private Color lostColor;
    private Color haveColor;
    private int yOffset;

    /**
     * Create a new health bar for the given entity.
     */
    public HealthBar(Entity owner) {
        super(Layer.HEALTH_BAR);
        this.owner = owner;
        lostColor = Color.GRAY;
        haveColor = Color.RED;
        yOffset = 10;
    }

    @Override
    public void update() {
        setWorldPos(owner.getWorldPos().add(new Vector3(0, owner.getHeight() + yOffset, 0)));
        setWorldRotation(Camera.getRotation());
    }

    /**
     * Set the color of the missing health
     *
     * @param c the color to set the missing health to
     */
    public void setLostColor(Color c) {
        lostColor = c;
    }

    /**
     * Set the color of the non-missing health
     *
     * @param c the color to set the non-missing health to
     */
    public void setHaveColor(Color c) {
        haveColor = c;
    }

    /**
     * Set the offset of this health bar's y-position relative to its owner.
     *
     * @param offset the offset of the bar on the y-axis
     */
    public void setYOffset(int offset) {
        yOffset = offset;
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
        image.setColor(lostColor);
        image.fill();
        image.setColor(haveColor);
        image.fillRect(0, 0, (int) (health / maxHealth * 30), 4);
        setOriginalImage(image);
    }
}
