import greenfoot.Greenfoot;

/**
 * Items are world sprites that can be held in the player inventory, used, and
 * dropped into the world.
 * 
 * @author Lucas Fu
 * @version June 2024
 */
public class Item extends WorldSprite {
    private Player player;
    private boolean isOnGround;
    private PhysicsController physics = new PhysicsController(this);
    private boolean qFlag = false;

    public Item(Player player, boolean isOnGround) {
        this.isOnGround = isOnGround;
        this.player = player;
    }

    @Override
    public void update() {
        if(isOnGround) awaitPickup();
        if(!isOnGround) awaitDrop();
    }

    public boolean isOnGround() {
        return isOnGround;
    }

    private void awaitPickup() {
        Vector3 playerPos = getWorld().getPlayer().getWorldPos();
        if(getWorldPos().distanceTo(playerPos) < 50) {
            physics.setMaxSpeed(3);
            physics.setMaxAccel(3);
            physics.accelTowards(playerPos);
        }
        if(getWorldPos().distanceTo(playerPos) < 10 && !qFlag) {
            player = getWorld().getPlayer();
            if(player.getHotbarSize() < Player.MAX_ITEM_NUM) {
                isOnGround = false;
                player.pickupItem(this);
            }
        }
    }

    private void awaitDrop() {
        if(Greenfoot.isKeyDown("q") && !qFlag) {
            physics.applyImpulse(new Vector3(0, 0, 1).rotateY(getPlayer().getWorldRotation()));
            getPlayer().throwItem();
            isOnGround = true;
            qFlag = true;
        } else if (!Greenfoot.isKeyDown("q") && qFlag) {
            qFlag = false;
        }
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Player getPlayer() {
        return player;
    }
}
