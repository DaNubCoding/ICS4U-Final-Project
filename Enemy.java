/**
 * An enemy is an entity that can interact with the player in some way.
 *
 * @author Andrew Wang
 * @version June 2024
 */
public abstract class Enemy extends Entity {
    private int noticeRange;
    private int forgetRange;
    private boolean noticed;
    private HealthBar healthBar;

    /**
     * Create a new enemy with the given Sprack sheet name.
     *
     * @param sheetName the Sprack sheet name
     */
    public Enemy(String sheetName) {
        super(sheetName);
        healthBar = new HealthBar(this);
    }

    @Override
    public void setHealth(double health) {
        super.setHealth(health);
        healthBar.setHealth(health);
    }

    /**
     * Hide the health bar.
     */
    public void hideHealth() {
        healthBar.hide();
    }

    /**
     * Show the health bar.
     */
    public void showHealth() {
        healthBar.show();
    }

    @Override
    public void addedToWorld(PixelWorld world) {
        ((SprackWorld) world).addWorldObject(healthBar,
                    ((SprackWorld) world).getPlayer().getWorldPos().add(new Vector3(0, 300, 0)));
    }

    @Override
    public void removedFromWorld(PixelWorld world) {
        world.removeSprite(healthBar);
    }

    @Override
    public void update() {
        Player player = getWorld().getPlayer();
        Vector3 playerPos = player.getWorldPos();
        Vector3 enemyPos = getWorldPos();

        // Check if player is within notice range
        if (playerPos.distanceTo(enemyPos) < noticeRange) {
            // Call notice method only once when player is first noticed
            if (!noticed) notice(player);
            noticed = true;
        // Check if player is outside forget range
        } else if (playerPos.distanceTo(enemyPos) > forgetRange) {
            // Call forget method only once when player is first forgotten
            if (noticed) forget(player);
            noticed = false;
        }

        if (noticed) {
            engage(player);
        } else {
            idle(player);
        }

        physics.update();
    }

    /**
     * The idle behavior of the enemy.
     * <p>
     * This is called continuously when the player is outside the notice range.
     *
     * @param player the player
     */
    public abstract void idle(Player player);

    /**
     * The notice behavior of the enemy.
     * <p>
     * This is called once when the player first enters the notice range.
     *
     * @param player the player
     */
    public abstract void notice(Player player);

    /**
     * The forget behavior of the enemy.
     * <p>
     * This is called once when the player first exits the forget range.
     *
     * @param player the player
     */
    public abstract void forget(Player player);

    /**
     * The engage behavior of the enemy.
     * <p>
     * This is called continuously when the player is within the notice range.
     *
     * @param player the player
     */
    public abstract void engage(Player player);

    /**
     * Aggro the enemy, this will set make the enemy notice the player.
     */
    public void aggro() {
        notice(getWorld().getPlayer());
        noticed = true;
    }

    /**
     * Check if the enemy is aggroed.
     *
     * @return true if the enemy is aggroed, false otherwise
     */
    public boolean isAggroed() {
        return noticed;
    }

    /**
     * Damage the enemy with the given damage.
     * <p>
     * This method is called when the enemy takes damage.
     * <p>
     * Override this method to add custom behavior when the enemy takes damage,
     * like parrying or immunity to certain types of damage.
     *
     * @param damage the damage
     */
    @Override
    public void damage(Damage damage) {
        super.damage(damage);
        if (damage.getOwner() instanceof Player)
            getWorld().getWorldData().addPlayerDamageDone(damage.getDamage());
        if (!isAggroed()) aggro();
    }

    /**
     * Set the notice range of the enemy.
     * <p>
     * This is a range in which the enemy will notice the player.
     *
     * @param range the notice range
     */
    public void setNoticeRange(int range) {
        noticeRange = range;
    }

    /**
     * Get the notice range of the enemy.
     *
     * @return the notice range
     */
    public int getNoticeRange() {
        return noticeRange;
    }

    /**
     * Set the forget range of the enemy.
     * <p>
     * This is a range outside of which the enemy will forget about the player.
     *
     * @param range the forget range
     */
    public void setForgetRange(int range) {
        forgetRange = range;
    }

    /**
     * Get the forget range of the enemy.
     *
     * @return the forget range
     */
    public int getForgetRange() {
        return forgetRange;
    }

    /**
     * Melee specifically the player within a certain range.
     *
     * @param damage the damage
     * @param range the range
     */
    public void meleePlayer(int damage, int range) {
        Damage meleeDamage = new Damage(this, this, damage, getWorldPos(), range);
        meleeDamage.setTarget(getWorld().getPlayer());
        getWorld().addDamage(meleeDamage);
    }
}
